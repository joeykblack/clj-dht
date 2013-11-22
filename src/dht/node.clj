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

(defn- forward-get [this map]
  (send-content
   (get-next this (:key map))
   {:get-value map}))

(defn get-value [this map]
  (if (responsible? this (:key map))
    (return-content (:return-to map)
                    (get (:map this) (:key map)))
    (forward-get this map)))


;; Put
;;    {put-value
;;      {:key key
;;       :value value}}

(defn- forward-put [this map]
  (send-content
   (get-next this (:key map))
   {:put-value map}))

(defn put-value [this map]
  (if (responsible? this map)
    (assoc-in this [:map (:key map)] (:value map))
    (forward-put this map)))


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




(defn test-method [this map]
  (info "test-method" this map))










