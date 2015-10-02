(ns de.otto.oscillator.routes
  (:require [compojure.core :as compojure]
            [compojure.route :as route]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kparams]
            [de.otto.oscillator.view.page :as page]))

(defn oscillator-routes [& {:keys [page-config chart-def-fetch-fun annotation-event-targets]}]
  (let [page-routes (page/page-routes page-config chart-def-fetch-fun annotation-event-targets)
        routes (conj page-routes (route/resources (:context-path page-config "/")))]
    (-> (apply compojure/routes routes)
        kparams/wrap-keyword-params
        params/wrap-params)))
