/**
 *  Copyright (c) 2015 Intel Corporation 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.trustedanalytics.atk.moduleloader.internal

import java.io.{ File, InputStream }
import java.nio.charset.Charset
import java.nio.file.{ Files, Paths }
import java.util.Scanner
import java.util.zip.ZipFile

import scala.collection.JavaConversions._

/**
 * Utility methods for working with Files.
 *
 * We've written our own utility methods here because the module-loader
 * project should avoid dependencies as much as possible so there won't
 * be any conflicts with module dependencies.
 */
private[internal] object ZipUtils {

  private val EMPTY_STRING = ""

  /**
   * Read a list of text files from a zip to Strings
   *
   * Non-existent files and empty files are read as the empty string.
   *
   * @param zipFile the zip file containing the text files
   * @param names the file names
   * @return each file contents as a String
   */
  def readFilesFromZip(zipFile: File, names: Seq[String]): Seq[String] = {
    val zip = new ZipFile(zipFile)
    names.map(name => {
      val entry = zip.getEntry(name)
      if (entry == null) {
        EMPTY_STRING
      }
      else {
        val inputStream = zip.getInputStream(entry)
        try {
          convertStreamToString(inputStream)
        }
        finally {
          inputStream.close()
        }
      }
    })
  }

  /**
   * Convert an InputStream to a String.
   *
   * InputStream is NOT closed by this method
   *
   * @param inputStream the input to read
   * @return the value of the InputStream as a String
   */
  def convertStreamToString(inputStream: InputStream): String = {
    val s = new Scanner(inputStream).useDelimiter("\\A")
    if (s.hasNext) {
      s.next()
    }
    else {
      EMPTY_STRING
    }
  }

}
