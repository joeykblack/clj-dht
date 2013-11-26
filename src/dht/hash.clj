(ns dht.hash
  (:import (java.security MessageDigest)))



(defn sha1-old [obj]
  "Return SHA1 of object as vector. Note: this does not work well over HTTP."
  (let [bytes (.getBytes (with-out-str (pr obj)))]
    (apply vector (.digest (MessageDigest/getInstance "SHA1") bytes))))


(defn sha1 [obj]
  "Return SHA1 of object as string."
  (let [bytes (.getBytes (with-out-str (pr obj)))]
    (format "%x" (new BigInteger (.digest (MessageDigest/getInstance "SHA1") bytes)))))




