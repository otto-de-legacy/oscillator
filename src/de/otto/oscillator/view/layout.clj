(ns de.otto.oscillator.view.layout
  (:require [hiccup.page :as h]
            [de.otto.oscillator.view.navigation :as link]
            [de.otto.oscillator.util.graphite-url :as url]))

(defn- page-navigation [context-path pages-def page-identifier url-params]
  [:ul {:class "page"}
   (for [page pages-def]
     [:li (link/page-link :path (str context-path (:url page))
                          :is-active (= (:url page) page-identifier)
                          :url-params url-params
                          :title (:name page))])])

(defn- env-navigation [environments url-params]
  [:ul {:class "environment"}
   (for [environment environments]
     [:li (link/nav-link url-params {:env (:key environment)} (:name environment))])])

(defn- main-navigation [context-path pages-def environments-def page-identifier url-params]
  [:nav {:class "top"}
   (page-navigation context-path pages-def page-identifier url-params)
   (env-navigation environments-def url-params)
   [:ul {:class "resolution"}
    [:li [:span {:class "descr"} "DATA RES:"]]
    [:li (link/nav-link url-params {:resolution "1min"} "1MIN")]
    [:li (link/nav-link url-params {:resolution "10min"} "10MIN")]
    [:li (link/nav-link url-params {:resolution "30min"} "30MIN")]
    [:li (link/nav-link url-params {:resolution "1hour"} "1HOUR")]]
   [:ul {:class "ymax-mode"}
    [:li [:span {:class "descr"} "YMAX:"]]
    [:li (link/nav-link url-params {:ymax-mode "free"} "FREE")]
    [:li (link/nav-link url-params {:ymax-mode "fix"} "FIX")]]
   [:ul {:class "stay"}
    [:li {:title "In STAY mode, the monitor will stay on the selected page indefinitely"}
     (link/nav-link url-params {:stay "true"} "STAY")]
    [:li {:title "In LEAVE mode, the monitor will go back to index page after some time"} 
     (link/nav-link url-params {:stay "false"} "LEAVE")]]])

(defn- time-navigation-from [url-params]
  [:nav {:class "side"}
   [:span {:class "currenttime"} "FROM"]
   [:ul {:class "time"}
    [:li (link/nav-link-scroll-button url-params :from -24 "<<")]
    [:li (link/nav-link-scroll-button url-params :from -1 "<")]
    [:li (link/time-display (:from url-params))]
    [:li (link/nav-link-scroll-button url-params :from +1 ">")]
    [:li (link/nav-link-scroll-button url-params :from +24 ">>")]

    [:li (link/nav-link-vertical-button url-params {:from "-1h" :resolution "1min"} "-1h")]
    [:li (link/nav-link-vertical-button url-params {:from "-6h" :resolution "10min"} "-6h")]
    [:li (link/nav-link-vertical-button url-params {:from "-24h" :resolution "30min"} "-24h")]
    [:li (link/nav-link-vertical-button url-params {:from "-96h" :resolution "1hour"} "-4d")]]])

(defn- time-navigation-until [url-params]
  [:nav {:class "side right"}
   [:span {:class "currenttime"} "UNTIL"]
   [:ul {:class "time"}
    [:li (link/nav-link-scroll-button url-params :until +24 ">>")]
    [:li (link/nav-link-scroll-button url-params :until +1 ">")]
    [:li (link/time-display (:until url-params))]
    [:li (link/nav-link-scroll-button url-params :until -1 "<")]
    [:li (link/nav-link-scroll-button url-params :until -24 "<<")]

    [:li (link/nav-link-vertical-button url-params {:until "-1min"} "0")]]])

(defn css-files [context-path]
  (into [] (map #(str context-path %)
                ["/stylesheets/rickshaw/layout.css"
                 "/stylesheets/rickshaw/graph.css"
                 "/stylesheets/rickshaw/detail.css"
                 "/stylesheets/rickshaw/legend.css"
                 "/stylesheets/rickshaw/annotations.css"
                 "/stylesheets/base.css"
                 "/stylesheets/navigation.css"
                 "/stylesheets/button.css"])))

(defn js-files [context-path]
  (into [] (map #(str context-path %)
                ["/javascript/vendor/d3.v3.js"
                 "/javascript/vendor/jquery-2.1.4.min.js"
                 "/javascript/vendor/rickshaw.js"
                 "/javascript/gen/oscillator.js"
                 "/javascript/piechart.js"])))

(defn common [& {:keys [context-path title pages environments page-identifier add-js-files add-css-files url-params content]}]
  (h/html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible"
             :content    "IE=edge,chrome=1"}]
     [:title title]
     [:link {:rel "icon" :type "image/png" :sizes "32x32" :href (str context-path "/favicon-32x32.png")}]
     [:link {:rel "icon" :type "image/png" :sizes "96x96" :href (str context-path "/favicon-96x96.png")}]
     [:link {:rel "icon" :type "image/png" :sizes "16x16" :href (str context-path "/favicon-16x16.png")}]
     (apply h/include-css (flatten (conj (css-files context-path) add-css-files)))
     (apply h/include-js (flatten (conj (js-files context-path) add-js-files)))]
    [:body
     (main-navigation context-path pages environments page-identifier url-params)
     [:header
      [:h1 title]]
     (time-navigation-from url-params)
     (time-navigation-until url-params)
     [:article {:id "content"} content]
     [:footer
      [:div "Made with ♥ by TESLA"]
      [:div (str url-params)]]]))
