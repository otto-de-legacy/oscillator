(ns de.otto.oscillator.view.page
  (:require [compojure.core :as compojure]
            [de.otto.oscillator.view.layout :as layout]
            [de.otto.oscillator.view.component :as vc]
            [clojure.data.json :as json]))

(def build-in-pages
  [{:url     "/detail"
    :heading ""
    :type    :detail}])

(defn- chart-def [chart-def-lookup-fun chart-name env]
  ((keyword chart-name) (chart-def-lookup-fun env)))

(defn render-pie-chart [{:keys [title data-fn colors]} page-config url-params]
  [:div {:class "col"}
   [:h2 title]
   [:div {:class                "piechart"
          :data-piechart-colors (json/write-str colors)
          :data-piechart        (json/write-str (data-fn page-config url-params))}]])

(defn- build-tile [{:keys [type params]} page-config chart-def-lookup-fun url-params]
  (case type
    :chart (let [chart-name (:chart-name params)
                 chart-def (chart-def chart-def-lookup-fun chart-name (:env url-params))]
             (vc/link-to-chart page-config chart-def chart-name url-params))
    :image (vc/image params)
    :number (vc/number params)
    :plain-html params
    :html-fn (params page-config url-params)
    :pie-chart (render-pie-chart params page-config url-params)))

(defn- build-page [page page-config chart-def-lookup-fun annotation-event-targets url-params]
  (case (:type page)
    :dashboard [:section
                (for [tile (:tiles page)]
                  (build-tile tile page-config chart-def-lookup-fun url-params))]
    :detail (let [chart-def (chart-def chart-def-lookup-fun (:chart url-params) (:env url-params))]
              [:section {:class "detail"}
               [:h2 (:chart url-params)]
               (vc/rickshaw-svg page-config chart-def annotation-event-targets url-params)
               (vc/plain-graphite-link page-config chart-def url-params)])))

(defn- page-route [page-config chart-def-lookup-fun annotation-event-targets page]
  (compojure/GET (str (:context-path page-config) (:url page)) {params :params}
    (let [url-params (merge (:default-params page-config) params)
          content (build-page page page-config chart-def-lookup-fun annotation-event-targets url-params)]
      (layout/common :context-path (:context-path page-config)
                     :title (:heading page)
                     :pages (:pages page-config)
                     :environments (:environments page-config)
                     :page-identifier (:url page)
                     :url-params url-params
                     :add-css-files (:add-css-files page-config)
                     :add-js-files (:add-js-files page-config)
                     :content content))))

(defn page-routes [page-config chart-def-lookup-fun annotation-event-targets]
  (map
    (partial page-route page-config chart-def-lookup-fun annotation-event-targets)
    (concat (:pages page-config) build-in-pages)))
