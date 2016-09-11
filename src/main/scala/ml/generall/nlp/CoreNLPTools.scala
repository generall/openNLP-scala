package ml.generall.nlp

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline._
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation
import collection.JavaConverters._
import scala.collection.mutable.ListBuffer


case class ChunkRecord(
                        word: String,
                        lemma: String,
                        pos: String,
                        ner: String,
                        parseTag: String,
                        groupId: Int,
                        beginPos: Int,
                        endPos: Int
                      ){}

class ChunkGroup(
                  val groupName: String,
                  var startIndex:Int,
                  var endIndex: Int
                ) {
  var tokens: Iterable[ChunkRecord] = List()
}


/**
  * Created by generall on 10.09.16.
  */
class CoreNLPTools {

  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse")
  val pipeline = new StanfordCoreNLP(props)

  case class ParseRecord(idx: Int, label: String )

  def groupBuff(tree: Tree, buff: ListBuffer[(String, Iterable[ParseRecord])]): Unit = {
    if (tree.isPrePreTerminal || tree.isPreTerminal) {
      val records = tree.getLeaves[Tree]
        .asScala
        .map(x => ParseRecord(
          x.label().asInstanceOf[CoreLabel].index() - 1,
          x.parent(tree).label().value())
        )
      buff.append((tree.label().value(), records))
    } else {
      tree.getChildrenAsList.asScala.foreach(x => groupBuff(x, buff))
    }
  }

  def groupTree(tree: Tree): List[(String, Iterable[ParseRecord])] = {
    val buff = new ListBuffer[(String, Iterable[ParseRecord])]
    groupBuff(tree, buff)
    buff.toList
  }


  def process(text: String): List[ChunkRecord] = {
    val document = new Annotation(text)
    pipeline.annotate(document)
    val sentences= document.get(classOf[SentencesAnnotation]).asScala
    assert(sentences.size == 1)
    val sentence = sentences.head

    val tree = sentence.get(classOf[TreeAnnotation]);
    val groups = groupTree(tree)
    groups.foreach(x => {
      print(s"${x._1}: ")
      println(x._2.mkString(" "))
    })

    val tokens = sentence.get(classOf[TokensAnnotation]).asScala.toArray

    groups.zipWithIndex.flatMap(pair =>{
      val (group, idx) = pair
      group._2.map(parseRecord => {
        val token = tokens(parseRecord.idx)
        val word = token.get(classOf[TextAnnotation])
        val lemma = token.get(classOf[LemmaAnnotation])
        // this is the POS tag of the token
        val pos = token.get(classOf[PartOfSpeechAnnotation])
        // this is the NER label of the token
        val ne = token.get(classOf[NamedEntityTagAnnotation])
        ChunkRecord(word, lemma, pos, ne, group._1, idx, token.beginPosition(), token.endPosition())
      })
    })


    //
    //      // this is the Stanford dependency graph of the current sentence
    //      val dependencies = sentence.get(classOf[CollapsedCCProcessedDependenciesAnnotation]);
    //      println("dependency graph:\n" + dependencies);
    //

  }
}
