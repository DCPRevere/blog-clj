(ns blog-clj.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults
             :refer [wrap-defaults site-defaults]]
            [environ.core :refer [env]]
            [hiccup.page :refer [html5 include-css]]
            [blog-clj.posts :as posts]
            [blog-clj.home :as home]
            [blog-clj.git :as git]
            [ring.middleware.ssl :as ssl]
            [ring.adapter.jetty :as jetty]))

(def head
  (if (env :dev)

   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    (include-css "/css/bootstrap.min.css"
                 "/css/site.css")
    [:link {:href "https://fonts.googleapis.com/css?family=Fira+Mono" :rel "stylesheet"}]
    [:link {:href "/css/fira-sans.css" :rel "stylesheet"}]
    [:script {:src "/js/jquery.min.js"}]
    [:script {:src "/js/bootstrap.min.js"}]]

   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    (include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
                 "/css/site.css")
    [:link {:href "https://fonts.googleapis.com/css?family=Fira+Mono" :rel "stylesheet"}]
    [:link {:href "https://fonts.googleapis.com/css?family=Fira+Sans" :rel "stylesheet"}]
    [:script {:src "https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"}]
    [:script {:src "http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"}]]))

(defn jumbotron []
  [:div.jumbotron.text-center
   [:h1 [:a {:href "/"} "hark@hrwd"]]
   [:h4 "hrwd's blog"]])

(defn containerise [div]
  [:body
   [:div.container
    (jumbotron)
    div]])

(defroutes handler
  (GET "/" []
       (html5 head (containerise (home/page))))

  (GET "/posts/:id" [id]
       (html5 head (containerise (posts/page id))))

  (route/resources "/")

  (route/not-found "Not Found"))

(defn enforce-ssl [handler]
  (if (or (env :dev) (env :test))
    handler
    (-> handler
        ssl/wrap-hsts
        ssl/wrap-ssl-redirect
        ssl/wrap-forwarded-scheme)))

(def site
  (-> handler
      enforce-ssl
      (wrap-defaults site-defaults)))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 3000))]
    (git/init)
    (jetty/run-jetty site {:port port :join? false})))

;; (.stop server)
;; (def server (-main))
