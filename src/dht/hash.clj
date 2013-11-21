(ns dht.hash
  (:import (java.security MessageDigest)))



(defn sha1 [obj]
  (let [bytes (.getBytes (with-out-str (pr obj)))]
    (apply vector (.digest (MessageDigest/getInstance "SHA1") bytes))))





