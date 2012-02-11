package eu.sbradl.liftedcontent.repository.snippet

import net.liftweb.util.Helpers._
import scala.xml.Text
import eu.sbradl.liftedcontent.repository.ContentRepository

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