package ml.generall.nlp

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline._
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation
import collection.JavaConverters._


/**
  * Created by generall on 10.09.16.
  */
class CoreNLPTools {

  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse")
  val pipeline = new StanfordCoreNLP(props)



  def searchInTree(tree: Tree, size: Int): Unit = {
    if(tree.isPrePreTerminal){
      if(tree.label().value() == "NP") {
        println (tree.getLeaves[Tree]
          .asScala
          .map(x => x.label().asInstanceOf[CoreLabel].word()).mkString(" "))
      }
    }else{
      tree.getChildrenAsList.asScala.foreach(x => searchInTree(x, size + 1))
    }

    /*
    if(tree.isLeaf){
      print(tree.label().asInstanceOf[CoreLabel].index())
    }
    println("-" * size ++ tree.label().value())
    */
  }

  def process(text: String) = {
    val document = new Annotation(text)
    pipeline.annotate(document)
    val sentences= document.get(classOf[SentencesAnnotation]).asScala
    sentences.foreach(sentence => {
      val tokens = sentence.get(classOf[TokensAnnotation]).asScala
      tokens.foreach(token =>{
        val word = token.get(classOf[TextAnnotation])
        // this is the POS tag of the token
        val pos = token.get(classOf[PartOfSpeechAnnotation])
        // this is the NER label of the token
        val ne = token.get(classOf[NamedEntityTagAnnotation])
        println("word: " + word + " pos: " + pos + " ne:" + ne)
      })

      // this is the parse tree of the current sentence
      val tree = sentence.get(classOf[TreeAnnotation]);

      searchInTree(tree, 0)
      println("parse tree:\n" + tree);

      // this is the Stanford dependency graph of the current sentence
      val dependencies = sentence.get(classOf[CollapsedCCProcessedDependenciesAnnotation]);
      println("dependency graph:\n" + dependencies);
    })
  }
}
