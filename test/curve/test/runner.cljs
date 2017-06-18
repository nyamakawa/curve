(ns curve.test.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [curve.test.core]
;              [your-project.util-test]
              ))

(doo-tests 'curve.test.core
;           'your-project.util-test
           )
