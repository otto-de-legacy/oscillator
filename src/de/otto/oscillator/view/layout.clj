(ns de.otto.oscillator.view.layout
  (:require [hiccup.page :as h]
            [de.otto.oscillator.view.navigation :as link]
            [de.otto.oscillator.util.graphite-url :as url]))

(defn- page-navigation [pages-def page-identifier url-params]
  [:ul {:class "page"}
   (for [page pages-def]
     [:li (link/page-link :path (:url page)
                          :is-active (= (:url page) page-identifier)
                          :url-params url-params
                          :title (:name page))])])

(defn- env-navigation [environments url-params]
  [:ul {:class "environment"}
   (for [environment environments]
     [:li (link/nav-link url-params {:env (:key environment)} (:name environment))])])

(defn- main-navigation [pages-def environments-def page-identifier url-params]
  [:nav {:class "top"}
   (env-navigation environments-def url-params)
   (page-navigation pages-def page-identifier url-params)
   [:ul {:class "resolution"}
    [:li [:span {:class "descr"} "DATA RES:"]]
    [:li (link/nav-link url-params {:resolution "1min"} "1MIN")]
    [:li (link/nav-link url-params {:resolution "10min"} "10MIN")]
    [:li (link/nav-link url-params {:resolution "30min"} "30MIN")]
    [:li (link/nav-link url-params {:resolution "1hour"} "1HOUR")]]
   [:ul {:class "ymax-mode"}
    [:li [:span {:class "descr"} "YMAX:"]]
    [:li (link/nav-link url-params {:ymax-mode "free"} "FREE")]
    [:li (link/nav-link url-params {:ymax-mode "fix"} "FIX")]]])

(defn- time-navigation-from [url-params]
  [:nav {:class "side"}
   [:ul {:class "time"}
    [:li (link/nav-link-without-state url-params {:from (url/move-time (:from url-params) - 24)} "<<")]
    [:li (link/nav-link-without-state url-params {:from (url/move-time (:from url-params) - 1)} "<")]
    [:li (link/nav-link url-params {:from "-6h"} "6h")]
    [:li (link/nav-link-without-state url-params {:from (url/move-time (:from url-params) + 1)} ">")]
    [:li (link/nav-link-without-state url-params {:from (url/move-time (:from url-params) + 24)} ">>")]]])

(defn- time-navigation-until [url-params]
  [:nav {:class "side right"}
   [:ul {:class "time"}
    [:li (link/nav-link-without-state url-params {:until (url/move-time (:until url-params) + 24)} ">>")]
    [:li (link/nav-link-without-state url-params {:until (url/move-time (:until url-params) + 1)} ">")]
    [:li (link/nav-link url-params {:until "-1min"} "0")]
    [:li (link/nav-link-without-state url-params {:until (url/move-time (:until url-params) - 1)} "<")]
    [:li (link/nav-link-without-state url-params {:until (url/move-time (:until url-params) - 24)} "<<")]]])

(def css-files
  ["/stylesheets/rickshaw/layout.css"
   "/stylesheets/rickshaw/graph.css"
   "/stylesheets/rickshaw/detail.css"
   "/stylesheets/rickshaw/legend.css"
   "/stylesheets/rickshaw/annotations.css"
   "/stylesheets/base.css"
   "/stylesheets/navigation.css"
   "/stylesheets/button.css"])

(def js-files
  ["/javascript/vendor/d3.v3.js"
   "/javascript/vendor/jquery-2.1.4.min.js"
   "/javascript/vendor/rickshaw.js"
   "/javascript/gen/oscillator.js"])

(defn common [& {:keys [title pages environments page-identifier add-js-files add-css-files url-params content]}]
  (h/html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible"
             :content    "IE=edge,chrome=1"}]
     [:title title]
     (apply h/include-css (flatten (conj css-files add-css-files)))
     (apply h/include-js (flatten (conj js-files add-js-files)))]
    [:body
     (main-navigation pages environments page-identifier url-params)
     [:header
      [:h1 {:class "container"} title]]
     (time-navigation-from url-params)
     (time-navigation-until url-params)
     [:article {:id "content"} content]
     [:footer
      [:div "Made with ♥ by TESLA"]
      [:div (prn-str url-params)]]]))
