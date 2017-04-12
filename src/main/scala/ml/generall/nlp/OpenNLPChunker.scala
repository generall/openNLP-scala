package ml.generall.nlp

import java.io.InputStream

import chalk.tools.chunker.{ChunkerME, ChunkerModel}
import chalk.tools.postag.{POSModel, POSTaggerME}
import chalk.tools.sentdetect.{SentenceDetectorME, SentenceModel}
import chalk.tools.tokenize.{TokenizerME, TokenizerModel}

/**
  * Created by generall on 16.08.16.
  */
class OpenNLPChunker extends Chunker{

  val tokenizerSteamModel : InputStream = getClass.getResourceAsStream("/en-token.bin")
  val posSteamModel : InputStream = getClass.getResourceAsStream("/en-pos-maxent.bin")
  val chunkerSteamModel : InputStream = getClass.getResourceAsStream("/en-chunker.bin")
  val sentSteamModel : InputStream = getClass.getResourceAsStream("/en-sent.bin")



  val tokenizerModel = new TokenizerModel(tokenizerSteamModel)
  val posModel = new POSModel(posSteamModel)
  val chunkerModel = new ChunkerModel(chunkerSteamModel)
  val sentModel = new SentenceModel(sentSteamModel)

  val tokenizer = new TokenizerME(tokenizerModel)
  val posTagger = new POSTaggerME(posModel)
  val chunker = new ChunkerME(chunkerModel)
  val sentDetector = new SentenceDetectorME(sentModel)


  override def chunk(sentences: List[String]): List[Array[(String, (String, String))]] = {
    val tokenGroups = sentences.map(tokenizer.tokenize)

    val tokens = tokenGroups.flatten.toArray
    val tags = posTagger.tag(tokens)
    val chunks = chunker.chunk(tokens, tags)

    var i = 0
    tokenGroups.map( group => {
      group.map( token => {
        val res = (token, (tags(i), chunks(i)))
        i += 1
        res
      })
    })
  }

  val pattern = "^I-".r

  val p = "[^-]+$".r

  override val acceptedGroups: Set[String] = Set()

  def group1(list: List[(String, (String, String))]): List[(String, List[(String, (String, String))])] = {
    list match {
      case head :: tail =>
        val (thisWords, others) = tail.span(x => pattern.findFirstIn(x._2._2).nonEmpty)
        val (word, (pos, chunk)) = head
        val chunkTag = p.findFirstIn(chunk).get
        (chunkTag, head :: thisWords) :: group1(others)
      case Nil => Nil
    }
  }


  override def group(sentences: List[String]): List[List[(String, List[(String, (String, String))])]] = {
    chunk(sentences).map(x => group1(x.toList))
  }

  override def chunkSentence(text: String): Array[String] = {
    sentDetector.sentDetect(text)
  }
}
