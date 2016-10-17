(defproject de.otto/oscillator "0.2.24"
            :description "A Clojure library that lets you create dashboards with
                         interactive charts to monitor applications in multiple environments."
            :url "https://github.com/otto-de/oscillator"
            :license {:name "Apache License 2.0"
                      :url  "http://www.apache.org/license/LICENSE-2.0.html"}
            :scm {:name "git"
                  :url  "https://github.com/otto-de/oscillator"}
            :plugins [[lein-cljsbuild "1.1.4"]]
            :dependencies [[org.clojure/clojure "1.8.0"]
                           [compojure "1.5.1"]
                           [hiccup "1.0.5"]
                           [org.clojure/data.json "0.2.6"]]
            :lein-release {:deploy-via :clojars}
            :cljsbuild {:builds {:app {:source-paths ["src-cljs"]
                                       :compiler     {:output-to     "resources/public/javascript/gen/oscillator.js"
                                                      :main          "de.otto.oscillator.prod"
                                                      :asset-path    "gen/out"
                                                      :jar           true
                                                      :optimizations :whitespace
                                                      :pretty-print  true}}}}
            :hooks [leiningen.cljsbuild]
            :profiles {:provided {:dependencies [[org.clojure/clojurescript "1.9.229"]
                                                 [cljs-ajax "0.5.8"]
                                                 [prismatic/dommy "1.1.0"]
                                                 [hipo "0.5.2"]]}
                       :dev      {:plugins [[lein-release/lein-release "1.0.9"]]}
                       :uberjar  {:aot :all}})
