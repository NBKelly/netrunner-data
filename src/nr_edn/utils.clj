(ns nr-edn.utils
  (:require [clojure.string :as string]))

(defmacro vals->vec
  ([coll]
   `(into [] (vals ~coll)))
  ([order coll]
   `(into [] (sort-by ~order (vals ~coll)))))

(defn cards->map
  ([cards] (cards->map :code cards))
  ([kw cards]
   (into {} (map (juxt kw identity) cards))))

(defn normalize-text [s]
  (some-> (not-empty s)
          (name)
          (java.text.Normalizer/normalize java.text.Normalizer$Form/NFD)
          (string/replace #"[\P{ASCII}]+" "")
          (string/trim)))

(defn slugify
  "As defined here: https://you.tools/slugify/"
  ([s] (slugify s "-"))
  ([s sep]
   (if (nil? s) ""
     (as-> s s
       (normalize-text s)
       (string/lower-case s)
       (string/split s #"[\p{Space}\p{Punct}]+")
       (filter seq s)
       (string/join sep s)))))

(defn prune-null-fields
  [card]
  (apply dissoc card (for [[k v] card :when (nil? v)] k)))
