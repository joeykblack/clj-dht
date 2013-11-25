; Start/stop node

; [k1 < k3 < 2] < 3 < [k2 < 1]

(use 'dht.net.serve)
(use 'dht.net.send)
(defonce server1 (make-node-server "http://localhost:3001" 3001))
(defonce server2 (make-node-server "http://localhost:3002" 3002))
(defonce server3 (make-node-server "http://localhost:3003" 3003))


(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3002"}})
(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3003"}})


(send-content-test "http://localhost:3001" {:log-node-state {}})
(send-content-test "http://localhost:3002" {:log-node-state {}})
(send-content-test "http://localhost:3003" {:log-node-state {}})


(send-content-test "http://localhost:3001" {:put-value {:key 'k1 :value 'v1}})

(send-content-test "http://localhost:3002" {:get-value {:key 'k1 :return-to "http://localhost:3001"}})


(send-content-test "http://localhost:3001" {:put-value {:key 'k2 :value 'v2}})

(send-content-test "http://localhost:3001" {:get-value {:key 'k2 :return-to "http://localhost:3001"}})


(send-content-test "http://localhost:3001" {:put-value {:key 'k3 :value 'v3}})

(send-content-test "http://localhost:3001" {:get-value {:key 'k3 :return-to "http://localhost:3001"}})



(send-content-test "http://localhost:3001" {:reset-node {:url "http://localhost:3001"}})

(.stop server1)
(.stop server2)
(.stop server3)

(.start server1)




























