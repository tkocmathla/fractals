(ns fractals.koch
  (:require
    [quil.core :as q]
    [quil.middleware :as qm])
  (:import
    [processing.core PVector]))

(def iterations 8)
(def theta (* 60 (/ Math/PI 180) -1))

(defn a [start _] (. start copy))
(defn b [start end] (doto (PVector/sub end start) (. div 3) (. add start)))
(defn c [start end] (let [v (doto (PVector/sub end start) (. div 3))]
                      (doto (. start copy)
                        (. add v)
                        (. add (. v (rotate theta))))))
(defn d [start end] (doto (PVector/sub start end) (. div 3) (. add end)))
(defn e [_ end] (. end copy))

(defn koch [[start end]]
  (when (< (q/frame-count) iterations)
    (partition 2 1 ((juxt a b c d e) start end))))

(defn setup []
  (q/frame-rate 2)
  {:lines [[(PVector. 0 (- (q/height) 20))
            (PVector. (q/width) (- (q/height) 20))]]})

(defn step [{:keys [lines]}]
  {:lines (mapcat koch lines)})

(defn draw [{:keys [lines]}]
  (when (seq lines)
    (q/background 255)
    (doseq [[start end] lines]
      (q/line (. start x) (. start y) (. end x) (. end y)))))

(q/defsketch koch-curve
  :title "Koch Curve"
  :size [1000 400]
  :setup setup
  :update step
  :draw draw
  :features [:keep-on-top]
  :middleware [qm/fun-mode])
