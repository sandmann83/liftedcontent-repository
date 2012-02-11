package eu.sbradl.liftedcontent.repository.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.FileParamHolder
import net.liftweb.http.S
import net.liftweb.common.Full
import java.io.File
import java.io.OutputStream
import java.io.FileOutputStream
import eu.sbradl.liftedcontent.repository.ContentRepository

class UploadFile {

  def render = {

    var fileHolder: FileParamHolder = null
    var repo = ""

    def process() = fileHolder match {
      case null => {
        S.warning(S ? "NO_FILE_SELECTED")
      }
      case _ => {
        val file = new File(ContentRepository.pathTo(repo, nextFuncName + extension(fileHolder.fileName)))

        if (file.exists) {
          S.notice(S ? ("FILE_ALREADY_EXISTS", fileHolder.fileName))
        } else {
          file.createNewFile
          
          val stream = new FileOutputStream(file)
          stream.write(fileHolder.file)
          stream.close
          
          S.notice(S ? ("FILE_UPLOADED", fileHolder.fileName))
        }

      }
    }

    val repos = (ContentRepository.repositories map (r => (r, r))).toSeq

    "name=repositories" #> SHtml.select(repos, Full(repos.head._1), repo = _) &
      "name=file" #> SHtml.fileUpload(fileHolder = _) &
      "type=submit" #> SHtml.submit(S ? "UPLOAD", process)
  }
  
  def extension(filename: String) = {
    val index = filename.lastIndexOf('.')
    filename.substring(index)
  }

}