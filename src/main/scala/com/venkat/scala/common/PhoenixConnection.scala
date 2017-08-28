package com.venkat.scala.common

import java.sql.{Connection, DriverManager, SQLException}
import javax.annotation.PostConstruct

/**
  * Created by venkatram.veerareddy on 8/28/2017.
  */
@org.springframework.context.annotation.Configuration
class PhoenixConnection {

  private var connection:Connection = _

  @PostConstruct
  @throws[Exception]
  def openConnection(): Unit = {
    connection = DriverManager.getConnection("jdbc:phoenix:xxx.xx.xx:/hbase-unsecure;user=xxx", "xxx", "xx")
  }

  def getConnection: Connection = connection

  def closeConnection(): Unit = {
    if (connection != null) try
      connection.close()
    catch {
      case e: SQLException =>
        throw new RuntimeException("unable to close connection ", e)
    }
  }

}
