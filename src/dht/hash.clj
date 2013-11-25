(ns dht.hash
  (:import (java.security MessageDigest)))



(defn sha-old [obj]
  (let [bytes (.getBytes (with-out-str (pr obj)))]
    (apply vector (.digest (MessageDigest/getInstance "SHA1") bytes))))






(defn sha1 [obj]
  (let [bytes (.getBytes (with-out-str (pr obj)))]
    (format "%x" (new BigInteger (.digest (MessageDigest/getInstance "SHA1") bytes)))))
