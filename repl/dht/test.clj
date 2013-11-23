; Start/stop node

(use 'dht.net.serve)
(use 'dht.net.send)
(defonce server1 (make-node-server "http://localhost:3001" 3001))

(defonce server2 (make-node-server "http://localhost:3002" 3002))
(send-content "http://localhost:3001" {:add-node {:url "http://localhost:3002"}})

(defonce server3 (make-node-server "http://localhost:3003" 3003))
(send-content "http://localhost:3001" {:add-node {:url "http://localhost:3003"}})


(send-content "http://localhost:3001" {:log-node-state {}})
(send-content "http://localhost:3002" {:log-node-state {}})
(send-content "http://localhost:3003" {:log-node-state {}})

(send-content "http://localhost:3001" {:put-value {:key 'k2 :value 'v2}})



(send-content "http://localhost:3001" {:reset-node {:url "http://localhost:3001"}})

(.stop server1)
(.stop server2)
(.start server1)


















