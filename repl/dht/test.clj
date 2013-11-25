; Start/stop node

; [k1 < k3 < 2] < 3 < [k2 < 1]

(use 'dht.net.serve)
(use 'dht.net.send)
(defonce server1 (make-node-server "http://localhost:3001" 3001))
(defonce server2 (make-node-server "http://localhost:3002" 3002))
(defonce server3 (make-node-server "http://localhost:3003" 3003))
(defonce server3 (make-node-server "http://localhost:3003" 3003))
(defonce server4 (make-node-server "http://localhost:3004" 3004))
(defonce server5 (make-node-server "http://localhost:3005" 3005))
(defonce server6 (make-node-server "http://localhost:3006" 3006))


(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3002"}})
(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3003"}})
(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3004"}})

(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3005"}})

(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3006"}})


(send-content-test "http://localhost:3001" {:log-node-state {}})
(send-content-test "http://localhost:3002" {:log-node-state {}})
(send-content-test "http://localhost:3003" {:log-node-state {}})
(send-content-test "http://localhost:3004" {:log-node-state {}})
(send-content-test "http://localhost:3005" {:log-node-state {}})

(send-content-test "http://localhost:3006" {:log-node-state {}})


(send-content-test "http://localhost:3001" {:log-node-state-full {}})
(send-content-test "http://localhost:3002" {:log-node-state-full {}})
(send-content-test "http://localhost:3003" {:log-node-state-full {}})
(send-content-test "http://localhost:3004" {:log-node-state-full {}})
(send-content-test "http://localhost:3005" {:log-node-state-full {}})
(send-content-test "http://localhost:3006" {:log-node-state-full {}})



(send-content-test "http://localhost:3001" {:put-value {:key "key1" :value "value1"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key2" :value "value2"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key3" :value "value3"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key4" :value "value4"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key5" :value "value5"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key6" :value "value6"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key7" :value "value7"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key8" :value "value8"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key9" :value "value9"}})




(send-content-test "http://localhost:3002" {:get-value {:key "key1" :return-to "http://localhost:3001"}})
(send-content-test "http://localhost:3001" {:get-value {:key "key2" :return-to "http://localhost:3001"}})
(send-content-test "http://localhost:3001" {:get-value {:key "key3" :return-to "http://localhost:3001"}})



(send-content-test "http://localhost:3001" {:reset-node {:url "http://localhost:3001"}})

(.stop server1)
(.stop server2)
(.stop server3)
(.stop server4)
(.stop server5)
(.stop server6)

(.start server1)


(use 'dht.hash)
(def sortedMap (sorted-map-by compare
                              (sha1 "http://localhost:3001") "http://localhost:3001"
                              (sha1 "http://localhost:3002") "http://localhost:3002"
                              (sha1 "http://localhost:3003") "http://localhost:3003"
                              (sha1 "http://localhost:3004") "http://localhost:3004"
                              (sha1 "http://localhost:3005") "http://localhost:3005"
                              (sha1 "http://localhost:3006") "http://localhost:3006"
                              (sha1 "key1") "key1"
                              (sha1 "key2") "key2"
                              (sha1 "key3") "key3"
                              (sha1 "key4") "key4"
                              (sha1 "key5") "key5"
                              (sha1 "key6") "key6"
                              (sha1 "key7") "key7"
                              (sha1 "key8") "key8"
                              (sha1 "key9") "key9"))
(map (comp println val) sortedMap)

































