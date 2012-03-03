package eu.sbradl.liftedcontent.repository

import eu.sbradl.liftedcontent.repository.lib.ContentProvider
import eu.sbradl.liftedcontent.util.Module

import java.io.File

import scala.Array.fallbackCanBuildFrom

import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.LiftRules

object ContentRepository extends Module {
  
  private var queries = Map[String, (String) => Seq[(String, String)]]()

  def name = "ContentRepository"
    
  lazy val contentDirectory = "upload"
    
  def register(repo: String, queryFunc: (String) => Seq[(String, String)]) {
    queries += ((repo, queryFunc))
    
    val dir = new File(pathTo(repo))
    dir.mkdir
  }
  
  def repositories = queries.keys
  
  def query(repo: String, term: String) = queries(repo)(term)
  
  override def init {
    LiftRules.statelessDispatchTable.append(ContentProvider)
    
    register("images", imageQuery)
  }
  
  def imageQuery(name: String): Seq[(String, String)] = {
    val dir = new File(pathTo("images"))
    
    dir.listFiles match {
      case null => List()
      case ls => {
        ls filterNot (_.getName.startsWith(".")) map (f => (f.getName, "/content/images/" + f.getName))
      }
    }
  }
  
  def pathTo(repo: String): String = contentDirectory + "/" + repo
  def pathTo(repo: String, file: String): String = pathTo(repo) + "/" + file
}