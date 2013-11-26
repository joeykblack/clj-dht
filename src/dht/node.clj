(ns dht.node
  (:use [dht.hash])
  (:use [dht.net.send])
  (:use [clojure.tools.logging :only (info debug error)]))

(defn make-node [url]
  {:url url ; url of this node
   :next url ; url of next node
   :last url ; url of last node
   :map {}}) ; map sha -> value


(defn- responsible? [this key]
  "Is this node responsible for key?"
  (let [myurl (:url this)
        mysha (sha1 myurl)
        last (:last this)
        shalast (sha1 last)]
    (or
     ; only one node
     (= myurl last)
     ; last node <= key < this node
     (and
      (<= (compare shalast key) 0)
      (< (compare key mysha) 0))
     ; this node < last node <= key (key is after largest node)
     (and
      (< (compare mysha shalast) 0)
      (<= (compare shalast key) 0))
     ; key < this node < last node (key is before smallest node)
     (and
      (< (compare key mysha) 0)
      (< (compare mysha shalast) 0)))))

(defn- get-next [this key]
  "Get next node. TODO: use finger table."
  (:next this))


(defn receive-content [this params]
  "Receive content. Format: {:receive-content {:value value that I asked for}}"
  (info "Receiving:" (:value params))
  this)


;; Get

(defn- forward-get [this params]
  (send-content
   (get-next this (:key params))
   {:get-value params}))

(defn get-value [this params]
  "Get value. Format: {:get-value {:key key :return-to where to return value}}"
  (let [sha (sha1 (:key params))]
    (if (responsible? this sha)
      (return-content (:return-to params)
                      {:receive-content
                       {:value (get (:map this) sha)}})
      (forward-get this params)))
  this)


;; Put

(defn- forward-put [this params]
  (send-content
   (get-next this (:key params))
   {:put-value params}))

(defn put-value [this params]
  "Put value in DHT. Format: {:put-value {:key key :value value}}"
  (let [sha (sha1 (:key params))]
    (if (responsible? this sha)
      (assoc-in this [:map sha] (:value params))
      (do (forward-put this params) this))))


;; Add Node

(defn- call-set-pointers [this url]
  (if (not= (:url this) (:last this))
    (send-content
     (:last this)
     {:update-node
      {:next url}}))
  (send-content
   url
   {:update-node
    {:last (:last this)
     :next (:url this)}}))

(defn- call-transfer-keys [this url]
  (send-content
   url
   {:update-node
    {:map (into {}
            (filter
             (fn [[k val]] (not (responsible? this k)))
             (:map this)))}}))

(defn- handle-add-node [this url]
  (call-set-pointers this url)
  (let [updated (assoc this :last url)] ; update last
    ; trasfer keys to new node
    (call-transfer-keys updated url)
    (assoc updated
      :map
      (into {}
            (filter
             (fn [[k val]] (responsible? updated k))
             (:map updated)))
      :next
      (if (= (:url this) (:last this))
        url
        (:next this)))))

(defn- call-add-node [this url]
  (send-content
   (get-next this (sha1 url))
   {:add-node
    {:url url}}))


(defn update-node [this params]
  "Merge in updates sent from other nodes."
  (merge this params))

(defn add-node [this params]
  "Add node to DHT. Format: {:add-node {:url URL of new node}}"
  (let [url (:url params)
        sha (sha1 url)]
    (if (responsible? this sha)
      (handle-add-node this url)
      (do (call-add-node this url) this))))


;; Util

(defn log-node-state [this params]
  "Log node pointers and values. Format: {:log-node-state {}}"
  (info "Node:" (:last this) "->" (:url this) "->" (:next this))
  (info "  Values:" (map val (:map this)))
  this)

(defn log-node-state-full [this params]
  "Log node pointers and map. Format: {:log-node-state-full {}}"
  (info "Node:" (:last this) "->" (:url this) "->" (:next this))
  (info "  Map:" (:map this))
  this)

(defn reset-node [this params]
  "Reset node"
  (make-node (:url params)))


































