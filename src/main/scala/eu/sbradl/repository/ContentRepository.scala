package eu.sbradl.repository

import de.sbradl.liftedcontent.util.Module
import java.io.File
import org.apache.derby.impl.store.raw.data.DirectActions
import net.liftweb.http.LiftRules
import net.liftweb.util.NamedPF
import net.liftweb.http.RewriteRequest
import net.liftweb.http.ParsePath
import net.liftweb.http.RewriteResponse
import net.liftweb.http.OutputStreamResponse
import net.liftweb.util.IoHelpers
import net.liftweb.http.InMemoryResponse
import eu.sbradl.repository.lib.ContentProvider

object ContentRepository extends Module {
  
  private var queries = Map[String, (String) => Seq[(String, String)]]()

  def name = "ContentRepository"
    
  lazy val contentDirectory = "upload"
    
  def register(repo: String, queryFunc: (String) => Seq[(String, String)]) {
    queries += ((repo, queryFunc))
    
    val dir = new File(pathTo(repo))
    dir.mkdir
  }
  
  def query(repo: String, term: String) = queries(repo)(term)
  
  override def init {
    LiftRules.statelessDispatchTable.append(ContentProvider)
    
    register("images", imageQuery)
  }
  
  def imageQuery(name: String): Seq[(String, String)] = {
    val dir = new File(pathTo("images"))
    
    dir.listFiles match {
      case null => List()
      case ls => ls map (f => (f.getName, "/content/images/" + f.getName))
    }
  }
  
  def pathTo(repo: String): String = contentDirectory + "/" + repo
  def pathTo(repo: String, file: String): String = pathTo(repo) + "/" + file
}