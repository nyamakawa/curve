(ns curve.math
  (:require ))

(defn abs [n]
   (.abs js/Math n))

(defn power [x n]
  (.pow js/Math x n))

(defn floor [x]
  (.floor js/Math x))

(defn round [x]
  (floor (+ 0.5 x)))

(defn fpart [x]
  (- x (floor x)))

(defn rfpart [x]
  (- 1 (fpart x)))
