(ns microblog-clojure.core
  (:require [ring.adapter.jetty :as j]
            [compojure.core :as c]
            [hiccup.core :as h])
  (:gen-class))

(c/defroutes app
  (c/GET "/" request
   (h/html [:html
            [:body
             [:form {:action "/add-message" :method "post"}
              [:input {:type "text" :placeholder "Enter Message" :name "message"}]
              [:button {:type "Submit"} "Add Message"]]]])))
(defonce server (atom nil))

(defn -main []
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty app {:port 8080 :join? false})))
