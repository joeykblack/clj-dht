(ns dht.net.send
  (:use [clojure.tools.logging :only (info debug error)])
  (:require [clj-http.client :as client]))

(defn send-content [url content]
  ;(debug url ":" content)
  (client/post url {:form-params {:dht content}
                    :content-type :json}))

(defn return-content [return-to content]
  ;(debug return-to ":" content)
  (client/post return-to {:form-params {:dht content}
                          :content-type :json}))


