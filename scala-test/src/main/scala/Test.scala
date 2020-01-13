import java.util.{HashMap => JavaHashMap}

import scala.collection.mutable.{HashMap => ScalaHashMap}

/**
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @date 2020/1/7 11:51
 * @version 1.0
 */
object Test {

  def main(args: Array[String]): Unit = {
    val scalaHashMap = new ScalaHashMap[String, Int]
    val javaHashMap = new JavaHashMap[String, Int]
    javaHashMap.put("a", 1)
    javaHashMap.put("b", 2)
    javaHashMap.put("c", 3)
    val iterator = javaHashMap.entrySet.iterator
    while (iterator.hasNext) {
      val entry = iterator.next
      scalaHashMap.put(entry.getKey, entry.getValue)
    }
    for (tuple <- scalaHashMap) {
      println(tuple._1 + "=>" + tuple._2)
    }
  }

  def multiTable(n: Int) = {
    for (i <- 1 to n) {
      for (j <- 1 to i) {
        printf("%s * %s = %s\t\t", j, i, j * i)
      }
      println()
    }
  }

}
