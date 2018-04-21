(ns curve.draw
  (:require [curve.math :as math]))

(defn draw-pixel [image data x y r g b & a]
  (if (and (<= x (.-width image)) (<= y (.-height image)))
    (let [offset (+ (* (math/round x) 4) (* (math/round y) (.-width image) 4))]
      ;(println "draw-pixel" x y r g b (first a))
      (aset data offset r)
      (aset data (+ offset 1) g)
      (aset data (+ offset 2) b)
      (aset data (+ offset 3) (or (first a) 255)))
    data))

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

(defn draw-line-inner [image data x0 y0 x1 y1 dx dy error x y ystep color steep]
;;  (println "draw-line-inner" x0 y0 x1 y1 dx dy error x y ystep color steep)
  (if (<= x x1)
    (do 
      (if steep
        (draw-pixel-color image data y x color)
        (draw-pixel-color image data x y color))
      (let [error (- error dy)]
        ;(println "error" error)
        (if (< error 0)
          (recur image data x0 y0 x1 y1 dx dy (+ error dx) (+ x 1) (+ y ystep) ystep color steep)
              (recur image data x0 y0 x1 y1 dx dy error (+ x 1) y ystep color steep))))))

(defn draw-line-steep [image data x0 y0 x1 y1 color steep]
;;  (println "draw-line-steep" x0 y0 x1 y1 steep)
  (if (> x0 x1)
    (recur image data x1 y1 x0 y0 color steep)
    (let [dx (- x1 x0)
          dy (math/abs (- y1 y0))
          error (/ dx 2)]
      (if (< y0 y1)
        (draw-line-inner image data x0 y0 x1 y1 dx dy error x0 y0 1 color steep)
        (draw-line-inner image data x0 y0 x1 y1 dx dy error x0 y0 -1 color steep)))))

(defn draw-line [image data x0 y0 x1 y1 color]
  (draw-pixel-color image data x0 y0 color)
  (draw-pixel-color image data x1 y1 color)
  (let [steep (> (math/abs (- y1 y0)) (math/abs (- x1 x0)))]
    (if steep
      (draw-line-steep image data y0 x0 y1 x1 color steep)
      (draw-line-steep image data x0 y0 x1 y1 color steep))))
      
