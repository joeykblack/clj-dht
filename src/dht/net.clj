(ns dht.net)

(defn send-content [ip content]
  (println)
  (println "To:" ip)
  (println "Content:" content))

(defn return-content [return-to content]
  (println)
  (println "Return to:" return-to)
  (println "Content:" content))



