(ns fractals.sierpinski
  (:require
    [quil.core :as q]
    [quil.middleware :as qm])
  (:import
    [processing.core PVector]))

(def iterations 8)
(def theta (* 60 (/ Math/PI 180) -1))
(defn halfv [v1 v2] (doto (PVector/sub v2 v1) (. div 2) (. add v1)))

(defn sierpinski [[v1 v2 v3]]
  (when (< (q/frame-count) iterations)
    [[v1 (halfv v1 v2) (halfv v1 v3)]
     [(halfv v1 v2) v2 (halfv v2 v3)]
     [(halfv v1 v3) (halfv v2 v3) v3]]))

(defn setup []
  (q/frame-rate 2)
  (let [v1 (PVector. 0 (- (q/height) 40))
        v3 (PVector. (q/width) (- (q/height) 40))
        v2 (doto (. v1 copy) (. add (. (PVector/sub v3 v1) (rotate theta))))]
    [[v1 v2 v3]]))

(defn draw [triangles]
  (when (seq triangles)
    (q/background 255)
    (doseq [[v1 v2 v3] triangles]
      (q/fill 0)
      (q/triangle (.x v1) (.y v1) (.x v2) (.y v2) (.x v3) (.y v3)))))

(q/defsketch sierpinski-triangle 
  :title "Sierpinski Triangle"
  :size [600 600]
  :setup setup
  :update (partial mapcat sierpinski)
  :draw draw
  :middleware [qm/fun-mode])
