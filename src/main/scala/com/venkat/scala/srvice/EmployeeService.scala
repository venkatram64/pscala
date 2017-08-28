package com.venkat.scala.srvice

import java.sql.{PreparedStatement, ResultSet, SQLException, Statement}
import java.util._
import java.util.{ArrayList, List}

import com.venkat.scala.common.PhoenixConnection
import com.venkat.scala.model.Employee
import com.venkat.scala.common.Names
import com.venkat.scala.model.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.util.Random

/**
  * Created by venkatram.veerareddy on 8/28/2017.
  */

@Service
class EmployeeService {

  @Autowired
  private val phoenixConnection:PhoenixConnection = null

  def getAllEmployees: List[Employee] = {
    val empList: List[Employee] = new ArrayList[Employee]
    try {
      val statement: Statement = phoenixConnection.getConnection.createStatement
      val resultSet: ResultSet = statement.executeQuery("select * from default.emp")
      while ( resultSet.next ) {
        val emp: Employee = new Employee
        emp.setId(resultSet.getInt("id"))
        emp.setFirstName(resultSet.getString("firstName"))
        emp.setLastName(resultSet.getString("lastName"))
        emp.setEmail(resultSet.getString("email"))
        emp.setPhoneNumber(resultSet.getString("phoneNumber"))
        empList.add(emp)
      }
    } catch {
      case ex: SQLException =>
        ex.printStackTrace()
    }
    empList
  }

  def addEmployee(employee: Employee): Employee = {
    try {
      val insertSQL = String.format("UPSERT INTO DEFAULT.EMP (id, firstName, lastName, email,phoneNumber) VALUES (%d, '%s','%s','%s','%s')",
        employee.getId.asInstanceOf[Object], employee.getFirstName.asInstanceOf[Object], employee.getLastName.asInstanceOf[Object],
        employee.getEmail.asInstanceOf[Object], employee.getPhoneNumber.asInstanceOf[Object])
      phoenixConnection.getConnection.setAutoCommit(false)
      val statement = phoenixConnection.getConnection.createStatement
      statement.execute(insertSQL)
      phoenixConnection.getConnection.commit()
    } catch {
      case ex: SQLException =>
        ex.printStackTrace()
    }
    employee
  }

  def getEmployeeByEmailId(email: String): Employee = {
    val emp = new Employee
    try {
      val sql = String.format("SELECT * FROM DEFAULT.EMP where email='%s'", email)
      val statement = phoenixConnection.getConnection.createStatement
      val rs = statement.executeQuery(sql)
      if (rs != null && rs.next) {
        emp.setId(rs.getInt("id"))
        emp.setFirstName(rs.getString("firstName"))
        emp.setLastName(rs.getString("lastName"))
        emp.setEmail(rs.getString("email"))
        emp.setPhoneNumber(rs.getString("phoneNumber"))
      }
    } catch {
      case ex: SQLException =>
        ex.printStackTrace()
    }
    emp
  }

  def deleteEmployee(empId: Integer): Boolean = {
    var deleted = false
    try {
      val sql = String.format("DELETE  FROM DEFAULT.EMP where id=%d", empId)
      phoenixConnection.getConnection.setAutoCommit(false)
      val statement = phoenixConnection.getConnection.createStatement
      statement.executeUpdate(sql)
      phoenixConnection.getConnection.commit()
      deleted = true
    } catch {
      case e: SQLException =>
        e.printStackTrace()
    }
    deleted
  }

  def bulkCreate(count:Int): Unit = {
    val names = new Names
    //this.getClass.getResourceAsStream("/avro/employee.avsc")
    val nameData = names.readFile("Names.txt")
    val DOMAIN_GROUPS = Array[String]("@google.com", "@hotmail.com", "@point72.com","@yahoo.com")

    var insertSQL:String = "UPSERT INTO DEFAULT.EMP (id, firstName, lastName, email,phoneNumber) VALUES (?,?,?,?,?)"

    var con = phoenixConnection.getConnection;
    con.setAutoCommit(false)
    var preparedStatement = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)
    val batchSize = 10000

    for(rec <- 1 to count) {
      val data = nameData.get(rec)
      var mail = DOMAIN_GROUPS(getRandomNumber(5))
      var emp = new Employee
      emp.setId(rec)
      emp.setFirstName(data._2)
      emp.setLastName(data._2)
      emp.setEmail(data._2+mail)
      emp.setPhoneNumber(genPhoneNumber)
      //addEmployee(emp)
      addBatch(preparedStatement, emp)
      //preparedStatement.addBatch()
      if (rec % batchSize == 0 || count == rec) {
        preparedStatement.executeBatch()
        con.commit()
      }
    }
    //val results = preparedStatement.executeBatch()
    //preparedStatement.close()
    //con.commit()

    //println("Added records are " + results.length)
  }

  def addBatch(preparedStatement:PreparedStatement, emp: Employee): Unit = {

    preparedStatement.setInt(1,emp.getId)
    preparedStatement.setString(2,emp.getFirstName)
    preparedStatement.setString(3,emp.getLastName)
    preparedStatement.setString(4,emp.getEmail)
    preparedStatement.setString(5,emp.getPhoneNumber)
    preparedStatement.addBatch()

  }

  private def genPhoneNumber: String = {
    val NUMBERS = Array[Int](1,2,3,4,5,6,7,8,9,0)
    val sb:StringBuilder = new StringBuilder
    for(i <- 1 to 10){
      sb.append(NUMBERS((getRandomNumber(10))))
      if(i % 3 == 0 && i % 9 != 0) sb.append("-")
    }
    sb.toString()
  }

  private def getRandomNumber(length: Int) = {
    var randomInt = 0
    val randomGenerator = new Random()
    randomInt = randomGenerator.nextInt(length)
    if (randomInt - 1 == -1) randomInt
    else randomInt - 1
  }


}
