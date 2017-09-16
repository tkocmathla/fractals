(ns fractals.tree
  (:require
    [quil.core :as q]
    [quil.middleware :as qm]))

(defn branch [len stroke angle]
  (when (> len 2)
    (q/stroke-weight stroke)
    (q/line 0 0 0 (* len -1))
    (q/translate 0 (* len -1))

    (q/push-matrix)
    (q/rotate angle)
    (branch (* len 0.66) (* stroke 0.6) angle)
    (q/pop-matrix)

    (q/push-matrix)
    (q/rotate (* angle -1))
    (branch (* len 0.66) (* stroke 0.6) (* angle -1))
    (q/pop-matrix)))

(defn draw [_]
  (q/background 255) 
  (q/translate (/ (q/width) 2) (q/height))
  (branch 150 20 (* 30 (/ Math/PI 180))))

(q/defsketch tree
  :title "Tree"
  :size [600 600]
  :setup setup
  :draw draw
  :middleware [qm/fun-mode])
