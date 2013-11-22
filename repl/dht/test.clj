(ns dht.test
  (:use [dht.node])
  (:use [dht.net]))

; Start/stop node

(use 'dht.net.serve)
(defonce server1 (make-node-server "http://localhost:3001" 3001))
(use 'dht.net.send)
(send-content "http://localhost:3001" {:test-method {:test 'test}})
(.stop server1)
(exit)

(.start server1)














