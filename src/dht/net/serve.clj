(ns dht.net.serve
  (:use dht.node)
  (:use ring.middleware.json
        ring.adapter.jetty)
  (:use [clojure.tools.logging :only (info debug error)]))


(defn- do-call [node keyval]
  (let [method (ns-resolve 'dht.node (symbol (name (key keyval))))
        values (val keyval)]
    (method node values)
    node))

(defn- process-request [node request]
  (let [calls (get-in request [:body :dht])]
    (if (not (nil? calls))
      (reduce do-call node calls)
      node)))

;; Make handler with persistent node
(defn- make-handler [url port]
  (let [node (atom (make-node url))]
    (wrap-json-body
     (fn [request]
       (dosync
        ;(info "Request:" request)
        (swap! node process-request request)
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body "OK"}))
     {:keywords? true})))



;; Return a server with persistent state
(defn make-node-server [url port]
   (run-jetty (make-handler url port) {:port port :join? false}))

(defn stop-node-server [server]
  (.stop server))

(defn start-node-server [server]
  (.start server))

