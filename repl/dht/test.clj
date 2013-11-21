(ns dht.test
  (:use [dht.node]))

(def node (make-node "1.0.0.1"))

(put-value node {:key :key1 :value :a})


(def node1 (make-node "1.0.0.2"))

(add-node node (:ip node1))







