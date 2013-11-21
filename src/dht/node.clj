(ns dht.node
  (:use [dht.hash])
  (:use [dht.net]))

(defn make-node [ip]
  {:ip ip ; ip of this node
   :next nil ; ip of next node
   :last nil ; ip of last node
   :map {}}) ; map sha -> value


(defn- responsible? [this key]
  (or (nil? (:last this))
    (and
     (>= (sha1 (:last this)) key) ; key >= last node
     (< key (sha1 (:ip this)))))) ; key < this node

(defn- get-next [this key]
  (:next this))


;; Get
;; {:type get-value
;;      :key key
;;      :return-to where to return value}

(defn- forward-get [this map]
  (send-content
   (get-next this (:key map))
   (assoc map :type 'get-value)))

(defn get-value [this map]
  (if (responsible? this (:key map))
    (return-content (:return-to map)
                    (get (:map this) (:key map)))
    (forward-get this map)))


;; Put
;;    {:type put-value
;;      :key key
;;      :value value}

(defn- forward-put [this map]
  (send-content
   (get-next this (:key map))
   (assoc map :type 'put-value)))

(defn put-value [this map]
  (if (responsible? this map)
    (assoc-in this [:map (:key map)] (:value map))
    (forward-put this map)))


;; Add Node

(defn set-pointers [this pointers]
  (merge this pointers))

(defn transfer-keys [this new-key-values]
  (merge this new-key-values))


(defn- call-set-pointers [this ip]
  (send-content
   (:last this)
   {:type 'set-pointers
    :next ip})
  (send-content
   ip
   {:type 'set-pointers
    :last (:last this)
    :next (:ip this)}))

(defn- call-transfer-keys [this ip]
  (send-content
   ip
   {:type 'transfer-keys
    :map (filter
          #(not (responsible? this (key %1)))
          (:map this))}))

(defn- handle-add-node [this ip]
  (call-set-pointers this ip)
  (let [updated (assoc this :last ip)] ; update last
    ; trasfer keys to new node
    (call-transfer-keys updated ip)
    (assoc updated :map
      (filter
       #(responsible? updated (key %1))
       (:map updated)))))

(defn- call-add-node [this ip]
  (send-content
   (get-next this ip)
   {:type 'add-node
    :ip ip}))

(defn add-node [this ip]
  (let [sha (sha1 ip)]
    (if (responsible? this sha)
      (handle-add-node this ip)
      (call-add-node this ip))))







