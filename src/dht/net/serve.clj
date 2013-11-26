(ns dht.net.serve
  (:use dht.node)
  (:use ring.middleware.json
        ring.adapter.jetty)
  (:use [clojure.tools.logging :only (info debug error)]))


(defn- do-call [node keyval]
  "Get method name and params and invoke."
  (let [method (ns-resolve 'dht.node (symbol (name (key keyval))))
        params (into {} (for [[k v] (val keyval)] [(keyword k) v]))]
    (if (nil? method)
      (do (error "Method" (key keyval) "does not exist!") node)
      (method node params))))

(defn- process-request [node request]
  "Iterate over (using reduce) and process all dht requests."
  (let [calls (get-in request [:body "dht"])]
    (if (not (nil? calls))
      (reduce do-call node calls) ; All node methods return an updated node
      node)))


(defn- make-handler [url port]
  "Make handler with persistent node"
  (let [node (agent (make-node url))]
    (wrap-json-body
     (fn [request]

       ; If there is an error log it and just reset state.
       (let [e (agent-error node)]
         (if (not (nil? e))
           (do
             (error "Error in node" (:url @node) ":" e)
             (restart-agent node @node))))

       ; Process request in new thread so we can return immediately
       ; Updated node is stored for next request
       (send node process-request request)

       {:status 200
        :headers {"Content-Type" "text/html"}
        :body "OK"}))))




(defn make-node-server [url port]
  "Return a server with persistent state"
   (run-jetty (make-handler url port) {:port port :join? false}))

(defn stop-node-server [server]
  "Stop node server. TODO: remove node from DHT."
  (.stop server))

(defn start-node-server [server]
  "Start node server."
  (.start server))



















