(defproject dht "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lein-kibit "0.0.8"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-jetty-adapter "1.2.0"]
                 [ring/ring-json "0.2.0"]
                 [clj-http "0.7.7"]
                 [org.clojure/data.json "0.2.3"]]
  :dev-dependencies [[ring-serve "0.1.2"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}}
  :plugins [[lein-midje "3.0.0"]
            [lein-ring "0.8.7"]]
  :aot :all
  :ring {:handler dht.net/app})



















