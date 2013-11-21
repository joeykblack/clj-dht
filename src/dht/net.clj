(ns dht.net
  (:use ring.middleware.params
        ring.util.response
        ring.adapter.jetty
        ring.middleware.json)
  (:require [clj-http.client :as client]))

(defn send-content [ip content]
  (println)
  (println "To:" ip)
  (println "Content:" content)
  (client/post ip {:form-params content
                   :content-type :json}))

(defn return-content [return-to content]
  (println)
  (println "Return to:" return-to)
  (println "Content:" content)
  (client/post return-to {:body (str content)}))

(defn handler [request]
  (.println System/out request)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World3"})

(def app
  (wrap-json-body handler {:keywords? true}))
