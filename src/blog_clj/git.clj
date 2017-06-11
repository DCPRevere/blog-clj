(ns blog-clj.git
  (:require [clojure.java.io :as io]
            [chime :refer [chime-at]]
            [clj-time.core :as t]
            [clj-time.periodic :refer [periodic-seq]]
            [clj-jgit.porcelain :as jgit]
            [cheshire.core :as json]
            [clojure.string :as str])
  (:import [java.io FileNotFoundException]))

(def gh-repo "https://github.com/dcprevere/blog.git")
(def local-repo "gh")

(defn get-repo
  "Either returns the existing repo or clones from GH."
  [gh-repo local-repo]
  (println "Attempting to load the repo...")
  (try (jgit/load-repo local-repo)
       (catch FileNotFoundException fne
         (println "Cloning repo...")
         (:repo (jgit/git-clone-full gh-repo local-repo)))))

(defn init
  ([]
   (init gh-repo local-repo))

  ([gh-repo local-repo]
   (println "Initialising git sourcing...")
   (let [repo (get-repo gh-repo local-repo)
         callback (fn [_]
                    (println "Executing callback...")
                    (println (.toString (jgit/git-pull repo))))]
     (chime-at (periodic-seq (t/now) (-> 10 t/seconds))
               callback
               {:error-handler (fn [e] (println e))}))))

(defn is-inside?
  [file dir]
  (.startsWith (.toAbsolutePath (.toPath file))
               (.toAbsolutePath (.toPath dir))))

(defn fetch-data []
  (into #{} (map #(-> % slurp (json/parse-string true))
                 (rest (remove #(is-inside? % (io/as-file (str local-repo "/.git")))
                               (file-seq (io/file local-repo)))))))
