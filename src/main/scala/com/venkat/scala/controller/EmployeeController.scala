package com.venkat.scala.controller

import com.venkat.scala.model.Employee
import com.venkat.scala.srvice.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation._
import java.util._

/**
  * Created by venkatram.veerareddy on 8/28/2017.
  */
@RestController
@RequestMapping(Array("employee"))
class EmployeeController {

  @Autowired
  private val employeeService:EmployeeService = null

  @RequestMapping(value = Array("/employees"), method = Array(RequestMethod.GET), produces = Array(APPLICATION_JSON_VALUE))
  def getAllEmployees: List[Employee] = employeeService.getAllEmployees

  @RequestMapping(value = Array("/createEmp"), method = Array(RequestMethod.POST), produces = Array(APPLICATION_JSON_VALUE))
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  def addEmployee(@RequestBody emp: Employee): Employee = employeeService.addEmployee(emp)

  @RequestMapping(value = Array("/{email:.+}"), method = Array(RequestMethod.GET), produces = Array(APPLICATION_JSON_VALUE))
  def findEmployeeById(@PathVariable("email") email: String): ResponseEntity[Employee] = ResponseEntity.ok.body(employeeService.getEmployeeByEmailId(email))

  @RequestMapping(value = Array("/{empId}"), method = Array(RequestMethod.DELETE), produces = Array(APPLICATION_JSON_VALUE))
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody def deleteEmployeeById(@PathVariable empId: Integer): ResponseEntity[String] = {
    val deleted = employeeService.deleteEmployee(empId)
    if (deleted) new ResponseEntity[String]("Unable to delete", HttpStatus.NO_CONTENT)
    else new ResponseEntity[String]("Deleted", HttpStatus.NO_CONTENT)
  }

}
