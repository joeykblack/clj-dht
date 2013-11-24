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
  (or (= (:url this) (:last this))
      ; last node <= key < this node
      (and
       (>= (compare (sha1 (:last this)) key) 0)
       (< (compare key (sha1 (:url this)))) 0)))

(defn- get-next [this key]
  (:next this))

;; Receive Content
;; {:receive-content
;;   {:value value that I asked for}}
(defn receive-content [this params]
  (info "Receiving:" (:value params))
  this)


;; Get
;; {:get-value
;;      {:key key
;;       :return-to where to return value}}

(defn- forward-get [this params]
  (send-content
   (get-next this (:key params))
   {:get-value params}))

(defn get-value [this params]
  (if (responsible? this (:key params))
    (return-content (:return-to params)
                    {:receive-content
                     {:value (get (:map this) (:key params))}})
    (forward-get this params))
  this)


;; Put
;;    {put-value
;;      {:key key
;;       :value value}}

(defn- forward-put [this params]
  (send-content
   (get-next this (:key params))
   {:put-value params}))

(defn put-value [this params]
  (if (responsible? this params)
    (assoc-in this [:map (:key params)] (:value params))
    (do (forward-put this params) this)))


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
    {:map (filter
           #(not (responsible? this (key %1)))
           (:map this))}}))

(defn- handle-add-node [this url]
  (call-set-pointers this url)
  (let [updated (assoc this :last url)] ; update last
    ; trasfer keys to new node
    (call-transfer-keys updated url)
    (assoc updated
      :map
      (filter
       #(responsible? updated (key %1))
       (:map updated))
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
  (merge this params))

(defn add-node [this params]
  (let [url (:url params)
        sha (sha1 url)]
    (if (responsible? this sha)
      (handle-add-node this url)
      (do (call-add-node this url) this))))


;; Util

(defn log-node-state [this params]
  (info "node-state:" this)
  this)

(defn reset-node [this params]
  (make-node (:url params)))




















