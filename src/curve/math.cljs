(ns curve.math
  (:require ))

(defn abs [n]
   (.abs js/Math n))

(defn power [x n]
   (.pow js/Math x n))
