import java.io.InputStream

import chalk.tools.chunker._
import chalk.tools.postag.{POSModel, POSTaggerME}
import chalk.tools.tokenize.{TokenizerME, TokenizerModel}
import chalk.tools.sentdetect.SentenceModel
import chalk.tools.sentdetect.SentenceDetectorME
import edu.stanford.nlp.simple._
import collection.JavaConverters._


/**
  * Created by generall on 20.07.16.
  */
object nlp extends App{



  def foo = {

    val tokenizerSteamModel : InputStream = getClass.getResourceAsStream("/en-token.bin")
    val posSteamModel : InputStream = getClass.getResourceAsStream("/en-pos-maxent.bin")
    val chunkerSteamModel : InputStream = getClass.getResourceAsStream("/en-chunker.bin")


    val tokenizerModel = new TokenizerModel(tokenizerSteamModel)
    val posModel = new POSModel(posSteamModel)
    val chunkerModel = new ChunkerModel(chunkerSteamModel)


    val tokenizer = new TokenizerME(tokenizerModel)
    val posTagger = new POSTaggerME(posModel)
    val chunker = new ChunkerME(chunkerModel)


    val tokens = tokenizer.tokenize("I will go to Kirov this weekend.")
    val tags = posTagger.tag(tokens)
    val chunks = chunker.chunk(tokens, tags)

    val all = (tokens zip (tags zip chunks)).toList

    val pattern = "^I-".r

    val p = "[^-]+$".r



    def group(list: List[(String, (String, String))]): List[(String, List[(String, (String, String))])] = {
      list match {
        case head :: tail =>
          val (thisWords, others) = tail.span(x => pattern.findFirstIn(x._2._2).nonEmpty)
          val (word, (pos, chunk)) = head
          val chunkTag = p.findFirstIn(chunk).get
          (chunkTag, head :: thisWords) :: group(others)
        case Nil => Nil
      }
    }

    val groups = group(all)

    groups.foreach(x => {
      println("tag: " ++ x._1)
      x._2.foreach(println)
      println(" --- ")
    })
  }

  def sentExample = {
    val sentSteamModel : InputStream = getClass.getResourceAsStream("/en-sent.bin")
    val sentModel = new SentenceModel(sentSteamModel)
    val sentDetector = new SentenceDetectorME(sentModel)

    val sents = ""

    val arr = sentDetector.sentDetect(sents)
    println(arr.size)
    arr.foreach(println)
  }

  def stemm = {
    val doc = new Document("Moving forward to Kirov. Testing of natural language processing.")
    doc.sentences().asScala.foreach(sent => {
      sent.lemmas().asScala.foreach(println)

    })
  }

  stemm

  //sentExample

}
