package ml.generall.nlp

import java.util.Properties

import collection.JavaConverters._

import edu.stanford.nlp.ling.CoreAnnotations.{TokensAnnotation, SentencesAnnotation}
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}

/**
  * Created by generall on 11.09.16.
  */

object Position extends Enumeration {
  type Position = Value
  val lefter, leftIntersect, includeFirst, includeSecond, rightIntersect, righter = Value
}

object Tools{

  import Position._

  def checkRange(rangeA: (Int, Int), rangeB: (Int, Int)): Position = {
    if( rangeA._2 < rangeB._1 ) return lefter
    if( rangeA._2 >= rangeB._1 && rangeB._2 > rangeA._2 && rangeA._1 < rangeB._1) return leftIntersect
    if( rangeB._1 <= rangeA._1 && rangeB._2 >= rangeA._2) return includeFirst
    if( rangeA._1 <= rangeB._1 && rangeA._2 >= rangeB._2) return includeSecond
    if( rangeB._2 >= rangeA._1 && rangeA._2 > rangeB._2 && rangeB._1 < rangeA._1) return rightIntersect
    if( rangeB._2 < rangeA._1 ) return righter
    throw new RuntimeException("Wrong intervals")
  }

}

class SentenceSplitter(){

  val splitProps = new Properties()
  splitProps.setProperty("annotators", "tokenize, ssplit")
  val splitPipeline = new StanfordCoreNLP(splitProps)

  def getSentence(text: String, mentionRange: (Int, Int)): Option[(Int, Int)] = {
    import Position._
    val document = new Annotation(text)
    splitPipeline.annotate(document)
    val sentences = document.get(classOf[SentencesAnnotation]).asScala
    sentences.foreach(sentence => {
      val tokens = sentence.get(classOf[TokensAnnotation]).asScala
      val sentRange = (tokens.head.beginPosition(), tokens.last.endPosition())
      if(Tools.checkRange(sentRange, mentionRange) == includeSecond)
        return Some(sentRange)
    })
    None
  }

}
