(ns fractals.koch
  (:require
    [quil.core :as q]
    [quil.middleware :as qm])
  (:import
    [processing.core PVector]))

(def iterations 8)
(def theta (* 60 (/ Math/PI 180) -1))

(defn a [v1 _] (. v1 copy))
(defn b [v1 v2] (doto (PVector/sub v2 v1) (. div 3) (. add v1)))
(defn c [v1 v2] (let [v (doto (PVector/sub v2 v1) (. div 3))]
                  (doto (. v1 copy)
                    (. add v)
                    (. add (. v (rotate theta))))))
(defn d [v1 v2] (doto (PVector/sub v1 v2) (. div 3) (. add v2)))
(defn e [_ v2] (. v2 copy))

(defn koch [[v1 v2]]
  (when (< (q/frame-count) iterations)
    (partition 2 1 ((juxt a b c d e) v1 v2))))

(defn setup []
  (q/frame-rate 2)
  [[(PVector. 0 (- (q/height) 20))
    (PVector. (q/width) (- (q/height) 20))]])

(defn draw [lines]
  (when (seq lines)
    (q/background 255)
    (doseq [[v1 v2] lines]
      (q/line (.x v1) (.y v1) (.x v2) (.y v2)))))

(q/defsketch koch-curve
  :title "Koch Curve"
  :size [1000 400]
  :setup setup
  :update (partial mapcat koch)
  :draw draw
  :middleware [qm/fun-mode])
