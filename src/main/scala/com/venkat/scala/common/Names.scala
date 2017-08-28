package com.venkat.scala.common

import java.io.{FileNotFoundException, IOException}
import scala.io.Source.fromFile
import scala.util.{Failure, Success, Try}

/**
  * Created by venkatram.veerareddy on 8/28/2017.
  */
class Names {

  private type NameData = (String,String)

  private def parseLine(line:String):NameData = {
    val parts = line.split(",")
    (parts(1), parts(3))
  }

  def readFile(fileName:String): Try[Array[NameData]] = {
    Try{
      val source = fromFile(fileName)
      val content = source.getLines().toArray
      val nameData = content.map(parseLine)
      nameData
    }
  }

}

object Names extends App{

  var names = new Names
  val nameData = names.readFile("Names.txt")
  //val list = nameData.get(0)
  nameData match {
    case Success(lines) =>{
      lines.foreach(d => println (d._1))
    }
  }

  //println(list._1 +", " + list._2)
  /*nameData match {
    case Success(lines) => {
      lines.foreach(println)
    }
    case Failure(f) => f match {
      case ex: FileNotFoundException => {
        println(s"Couldn't find the file ${ex.getMessage()}")
      }
      case ex: IOException => {
        println(s"Read failed. ${ex.getMessage()}")
      }
    }
  }*/

}
