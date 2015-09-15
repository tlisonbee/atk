/*
// Copyright (c) 2015 Intel Corporation 
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/

package org.trustedanalytics.atk.domain.frame

import org.trustedanalytics.atk.engine.plugin.{ ArgDoc, Invocation }

case class ClassificationMetricArgs(frame: FrameReference,
                                    @ArgDoc("""The name of the column containing the
correct label for each instance.""") labelColumn: String,
                                    @ArgDoc("""The name of the column containing the
predicted label for each instance.""") predColumn: String,
                                    @ArgDoc("""The name of an optional column containing the
frequency of observations.""") frequencyColumn: Option[String] = None,
                                    @ArgDoc("""This is a str or int for binary classifiers,
and Null for multi-class classifiers.
The value to be interpreted as a positive instance.""") posLabel: Option[Either[String, Int]],
                                    @ArgDoc("""This is the beta value to use for
:math:`F_{ \beta}` measure (default F1 measure is computed); must be greater than zero.
Defaults is 1.""") beta: Option[Double] = None) {
  require(frame != null, "ClassificationMetric requires a non-null dataframe.")
  require(labelColumn != null && !labelColumn.equals(""), "label column is required")
  require(predColumn != null && !predColumn.equals(""), "predict column is required")
  beta match {
    case Some(x) => require(x >= 0, "invalid beta value for f measure. Should be greater than or equal to 0")
    case _ => null
  }
}


/**
 * Entry in confusion matrix
 *
 * @param predictedClass Predicted class label
 * @param actualClass Actual class label
 * @param count Count of instances matching actual class and predicted class
 */
case class ConfusionMatrixEntry(predictedClass: String, actualClass: String, count: Long)

/**
 * Classification metrics
 *
 * @param fMeasure Weighted average of precision and recall
 * @param accuracy Fraction of correct predictions
 * @param recall Fraction of positives correctly predicted
 * @param precision Fraction of correct predictions among positive predictions
 * @param confusionMatrix Matrix of actual vs. predicted classes
 */
case class ClassificationMetricValue(fMeasure: Double,
                                     accuracy: Double,
                                     recall: Double,
                                     precision: Double,
                                     confusionMatrix: List[ConfusionMatrixEntry])
