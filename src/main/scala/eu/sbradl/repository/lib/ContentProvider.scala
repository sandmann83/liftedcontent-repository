package eu.sbradl.repository.lib

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.Req
import net.liftweb.util.IoHelpers
import java.io.File
import eu.sbradl.repository.ContentRepository
import net.liftweb.http.InMemoryResponse
import net.liftweb.http.NotFoundResponse

object ContentProvider extends RestHelper {

  serve {
    case Req("content" :: repo :: resourceName :: Nil, extension, _) => {
      val filename: String = "%s.%s".format(resourceName, extension)
      val file = new File(ContentRepository.pathTo(repo, filename))

      if (file.exists) {
        val data = IoHelpers.readWholeFile(file)
        new InMemoryResponse(data, Nil, Nil, 200)
      } else {
        NotFoundResponse("File '%s' not found in repository '%s'".format(filename, repo))
      }

    }
  }

}