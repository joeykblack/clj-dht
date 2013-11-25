# dht 0.1.0

by [Joey K Black](joey-black.appspot.com)

Distributed hash table implementation

## Usage

To start node servers:
```clojure
(use 'dht.net.serve)
(defonce server1 (make-node-server "URL" port))
(defonce server2 (make-node-server "http://localhost:3002" 3002))
(defonce server3 (make-node-server "http://localhost:3003" 3003))
```

To add nodes to the DHT:
```clojure
(use 'dht.net.send)
(send-content-test "URL of a node in the DHT" {:add-node {:url "new URL"}})
(send-content-test "http://localhost:3001" {:add-node {:url "http://localhost:3002"}})
```

To output state of a node:
```clojure
(send-content-test "http://localhost:3001" {:log-node-state {}})
```

To put a value in the DHT:
```clojure
(send-content-test "URL of any node in the in the DHT" {:put-value {:key "key" :value "value"}})
(send-content-test "http://localhost:3001" {:put-value {:key "key1" :value "value1"}})
```

To get a value from the DHT:
```clojure
(send-content-test "URL of any node in the in the DHT" {:get-value {:key "key1" :return-to "URL of node to return result to"}})
(send-content-test "http://localhost:3001" {:get-value {:key "key1" :return-to "http://localhost:3001"}})
```

To stop a node server:
```clojure
(.stop server1)
```


## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.





