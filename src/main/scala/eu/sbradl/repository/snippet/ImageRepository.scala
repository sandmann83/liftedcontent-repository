package eu.sbradl.repository.snippet

import net.liftweb.util.Helpers._
import eu.sbradl.repository.ContentRepository
import scala.xml.Text

class ImageRepository {

  def render = {
    "data-lift-id=item *" #> ContentRepository.imageQuery("").map {
      img => {
        "img [src]" #> img._2 &
        "img [alt]" #> img._1
      } 
    }

  }

}