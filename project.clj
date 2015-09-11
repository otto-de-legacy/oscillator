(defproject de.otto/oscillator "0.2.8"
            :description "A Clojure library that lets you create dashboards with
                         interactive charts to monitor applications in multiple environments."
            :url "https://github.com/otto-de/oscillator"
            :license {:name "Apache License 2.0"
                      :url  "http://www.apache.org/license/LICENSE-2.0.html"}
            :scm {:name "git"
                  :url  "https://github.com/otto-de/oscillator"}
            :plugins [[lein-cljsbuild "1.0.6"]]
            :dependencies [[org.clojure/clojure "1.7.0"]
                           [compojure "1.3.4"]
                           [hiccup "1.0.5"]
                           [org.clojure/data.json "0.2.6"]]
            :cljsbuild {:builds {:app {:source-paths ["src-cljs"]
                                       :compiler     {:output-to     "resources/public/javascript/gen/oscillator.js"
                                                      :main          "de.otto.oscillator.prod"
                                                      :asset-path    "gen/out"
                                                      :jar           true
                                                      :optimizations :whitespace
                                                      :pretty-print  true}}}}
            :hooks [leiningen.cljsbuild]
            :profiles {:provided {:dependencies [[org.clojure/clojurescript "1.7.48"]
                                                 [cljs-ajax "0.3.13"]
                                                 [prismatic/dommy "1.1.0"]
                                                 [hipo "0.4.0"]]}
                       :uberjar  {:aot :all}})
