(ns dht.net.send
  (:use [clojure.tools.logging :only (info debug error)])
  (:require [clj-http.client :as client]))


(defn send-content [url content]
  "Wrap content as json and send to URL"
  (let [resp (client/post url {:form-params {:dht content}
                               :content-type :json})]
    (if (not= 200 (:status resp))
      (error resp))))

(defn return-content [return-to content]
  "Wrap content as json and send to URL"
  (let [resp (client/post return-to {:form-params {:dht content}
                                     :content-type :json})]
    (if (not= 200 (:status resp))
      (error resp))))

(defn send-content-test [url content]
  "Wrap content as json and send to URL"
  (let [resp (client/post url {:form-params {:dht content}
                               :content-type :json})]
    (if (not= 200 (:status resp))
      (error resp))))









