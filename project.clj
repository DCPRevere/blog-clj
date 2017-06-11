(defproject blog-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[cheshire "5.7.1"]
                 [clj-jgit "0.8.9"]
                 [clj-time "0.13.0"]
                 [compojure "1.6.0"]
                 [environ "1.1.0"]
                 [jarohen/chime "0.2.1"]
                 [org.clojure/clojure "1.8.0"]
                 [ring/ring-defaults "0.3.0"]
                 [ring/ring-jetty-adapter "1.6.1"]
                 [ring/ring-ssl "0.3.0"]]
  :plugins [[lein-environ "1.1.0"]
            [lein-ring "0.9.7"]
            [environ/environ.lein "0.3.1"]]
  :ring {:init blog-clj.git/init
         :handler blog-clj.web/site}
  :hooks [environ.leiningen.hooks]
  :uberjar-name "blog-clj-standalone.jar"
  :profiles {:production {:env {:production true}}
             :dev {:env {:dev true}
                   :dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]]}})
