(ns dht.net.serve
  (:use dht.node)
  (:use ring.middleware.json
        ring.adapter.jetty)
  (:use [clojure.tools.logging :only (info debug error)]))


(defn- do-call [node keyval]
  (let [method (ns-resolve 'dht.node (symbol (name (key keyval))))
        values (into {} (for [[k v] (val keyval)] [(keyword k) v]))]
    (if (nil? method)
      (do (error "Method" (key keyval) "does not exist!") node)
      (method node values))))

(defn- process-request [node request]
  (let [calls (get-in request [:body "dht"])]
    ;(info "request" calls)
    (if (not (nil? calls))
      (reduce do-call node calls)
      node)))

;; Make handler with persistent node
(defn- make-handler [url port]
  (let [node (agent (make-node url))]
    (wrap-json-body
     (fn [request]
       (let [e (agent-error node)]
         (if (not (nil? e))
           (do
             (error "Error in node" (:url @node) ":" e)
             (restart-agent node @node))))
       ;(info "Start:" (:url @node))
       (send node process-request request)
       ;(info "End:" (:url @node))
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body "OK"})
     '{:keywords? true} )))



;; Return a server with persistent state
(defn make-node-server [url port]
   (run-jetty (make-handler url port) {:port port :join? false}))

(defn stop-node-server [server]
  (.stop server))

(defn start-node-server [server]
  (.start server))















