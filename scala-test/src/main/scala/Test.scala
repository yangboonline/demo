/**
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @date 2020/1/7 11:51
 * @version 1.0
 */
object Test {

  def main(args: Array[String]): Unit = {
    multiTable(9);
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