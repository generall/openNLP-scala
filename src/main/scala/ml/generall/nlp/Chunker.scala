package ml.generall.nlp

/**
  * Created by generall on 16.08.16.
  */


trait Chunker {

  val acceptedGroups: Set[String]

  def chunk(sentences: List[String]): List[Array[(String, (String, String))]]

  def group(sentences: List[String]): List[List[(String, List[(String, (String, String))])]]

  def chunkSentence(text: String): Array[String]

}
