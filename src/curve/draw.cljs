(ns curve.draw
  (:require [curve.math :as math]))

(defn draw-pixel [image data x y r g b & a]
;  (println "draw-pixel" x y r g b (first a))
  (let [offset (+ (* x 4) (* y (.-width image) 4))]
    (aset data offset r)
    (aset data (+ offset 1) g)
    (aset data (+ offset 2) b)
    (aset data (+ offset 3) (or (first a) 255)))
  data)

(defn draw-pixel-color [image data x y color]
  (let [length (count color)]
    (cond
      (= length 1)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 0)
                  (nth color 0)
                  255)
      (= length 2)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 0)
                  (nth color 0)
                  (nth color 1))
      (= length 3)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 1)
                  (nth color 2)
                  255)
      (= length 4)
      (draw-pixel image data x y
                  (nth color 0)
                  (nth color 1)
                  (nth color 2)
                  (nth color 3))))
  data)

(defn draw-line-inner [image data x1 y1 x2 y2 dx dy error x y ystep color steep]
;  (println "draw-line-inner" x1 y1 x2 y2 dx dy error x y ystep color steep)
  (if (<= x x2)
    (do 
      (if steep
        (draw-pixel-color image data y x color)
        (draw-pixel-color image data x y color))
      (let [error (- error dy)]
        ;(println "error" error)
        (if (< error 0)
          (recur image data x1 y1 x2 y2 dx dy (+ error dx) (+ x 1) (+ y ystep) ystep color steep)
              (recur image data x1 y1 x2 y2 dx dy error (+ x 1) y ystep color steep))))))

(defn draw-line [image data x1 y1 x2 y2 color]
  (let [steep (> (math/abs (- y2 y1)) (math/abs (- x2 x1)))]
    (if steep
      (recur image data y1 x1 y2 x2 color)
      (if (> x1 x2)
        (recur image data y2 x2 y1 x1 color)
        (let [dx (- x2 x1)
              dy (- y2 y1)
              error (/ dx 2)]
          (if (< y1 y2)
            (draw-line-inner image data x1 y1 x2 y2 dx dy error x1 y1 1 color steep)
            (draw-line-inner image data x1 y1 x2 y2 dx dy error x1 y1 -1 color steep)))))))

(defn draw-curve [sx sy ex ey c1x c1y c2x c2y c3x c3y c4x c4y]
  )

(defn bezier [t s e x1 x2 x3 x4]
  (+ (* (math/power (- 1 t) 3) s)
     (* 3 (math/power (- 1 t) 2) t x1)
     (* 6 (- 1 t) (math/power t 2) x2)
     (* (math/power t 3 ) x3)))
