(ns curve.bezier
  (:require [curve.math :as math]))

(defn bezier [t x1 x2 x3 x4]
  (+ (* (math/power (- 1 t) 3) x1)
     (* 3 (math/power (- 1 t) 2) t x2)
     (* 3 (- 1 t) (math/power t 2) x3)
     (* (math/power t 3 ) x4)))

(defn close-enough? [x1 y1 x y]
  (println "d " (+ (math/power (- x x1) 2)
          (math/power (- y y1) 2.0)) x1 y1 x y)
  (> 1.0 (+ (math/power (- x x1) 2.0)
          (math/power (- y y1) 2.0))))

;; (defn draw-curve-inner [t1 t2 sx sy ex ey c1x c1y c2x c2y  draw-line]
;;   (let [t (/ (- t2 t1) 2)
;;         x (bezier t sx c1x c2x ex)
;;         y (bezier t sy c1y c2y ey)
;;         x1 (bezier t1 sx c1x c2x ex)
;;         y1 (bezier t1 sy c1y c2y ey)
;;         x2 (bezier t2 sx c1x c2x ex)
;;         y2 (bezier t2 sy c1y c2y ey)]
;;     (println "t" t "t1" t1 "t2" t2 "x" x "y" y "x1" x1 "y1" y1 "x2" x2 "y2" y2)
;;     (if (close-enough? x1 y1 x2 y2)
;;       (draw-line x1 y1 x2 y2)
;;       (do 
;;         (draw-curve-inner t1 t sx sy ex ey c1x c1y c2x c2y draw-line)
;;         (recur t t2 sx sy ex ey c1x c1y c2x c2y draw-line)
;; ))))

;; (defn draw-curve [sx sy ex ey c1x c1y c2x c2y draw-line]
;;   (draw-curve-inner 0 1.0 sx sy ex ey c1x c1y c2x c2y draw-line))

(defn draw-curve-inner [t step prev-x prev-y sx sy ex ey c1x c1y c2x c2y  draw-line]
  (if (< t 1.0) 
    (let [x (bezier t sx c1x c2x ex)
          y (bezier t sy c1y c2y ey)]
      (do 
        ;(println "draw-line" prev-x prev-y x y)
        (draw-line prev-x prev-y x y))        
      (recur (+ t step) step x y sx sy ex ey c1x c1y c2x c2y draw-line))))

(defn draw-curve [sx sy ex ey c1x c1y c2x c2y draw-line]
  (draw-curve-inner 0 0.001 sx sy sx sy ex ey c1x c1y c2x c2y draw-line))
