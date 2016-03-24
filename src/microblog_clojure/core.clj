(ns microblog-clojure.core
  (:require [ring.adapter.jetty :as j]
            [compojure.core :as c]
            [hiccup.core :as h]
            [ring.middleware.params :as p]
            [ring.util.response :as r])
  (:gen-class))

(defonce messages (atom[]))
(defonce server (atom nil))

(c/defroutes app
  (c/GET "/" request
   (h/html [:html
            [:body
             [:form {:action "/add-message" :method "post"}
              [:input {:type "text" :placeholder "Enter Message" :name "message"}]
              [:button {:type "Submit"} "Add Message"]]
             [:ul
              (map (fn [message]
                     [:li message])
                @messages)]]]))

  (c/POST "/add-message" request
    (let [message (get (:params request) "message")]
      (swap! messages conj message)
      (r/redirect "/"))))

(defn -main []
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty (p/wrap-params app) {:port 8080 :join? false})))
