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
       (>= (sha1 (:last this)) key)
       (< key (sha1 (:url this))))))

(defn- get-next [this key]
  (:next this))


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
                    (get (:map this) (:key params)))
    (forward-get this params)))


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
    (do
      (info "Input:" this params)
      (assoc-in this [:map (:key params)] (:value params)))
    (forward-put this params)))


;; Add Node

(defn set-pointers [this pointers]
  (merge this pointers))

(defn transfer-keys [this new-key-values]
  (merge this new-key-values))


(defn- call-set-pointers [this url]
  (send-content
   (:last this)
   {:set-pointers
    {:next url}})
  (send-content
   url
   {:set-pointers
    {:last (:last this)
     :next (:url this)}}))

(defn- call-transfer-keys [this url]
  (send-content
   url
   {:transfer-keys
    {:map (filter
           #(not (responsible? this (key %1)))
           (:map this))}}))

(defn- handle-add-node [this url]
  (call-set-pointers this url)
  (let [updated (assoc this :last url)] ; update last
    ; trasfer keys to new node
    (call-transfer-keys updated url)
    (assoc updated :map
      (filter
       #(responsible? updated (key %1))
       (:map updated)))))

(defn- call-add-node [this url]
  (send-content
   (get-next this url)
   {:add-node
    {:url url}}))

(defn add-node [this url]
  (let [sha (sha1 url)]
    (if (responsible? this sha)
      (handle-add-node this url)
      (call-add-node this url))))




(defn test-method [this params]
  (info "test-method" this params))












