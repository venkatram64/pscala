package com.venkat.scala;

import com.venkat.scala.common.PhoenixConnection
import com.venkat.scala.model.Employee
import com.venkat.scala.srvice.EmployeeService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Assert._

@ActiveProfiles(Array("test"))
@RunWith(classOf[SpringRunner])
@SpringBootTest(classes = Array(classOf[PhoenixConnection], classOf[EmployeeService]))
@EnableConfigurationProperties
class EmployeeServiceTest {


	@Autowired
	val phoenixConnection:PhoenixConnection = null

	@Autowired
	val employeeService:EmployeeService = null

	@Test
	def addRecord(): Unit = {
		val emp = new Employee
		emp.setId(8)
		emp.setEmail("venkat@hotmail.com")
		emp.setFirstName("Venkat")
		emp.setLastName("Veera")
		emp.setPhoneNumber("909-555-9990")
		val createdEmp = employeeService.addEmployee(emp)
		assertEquals("emp id comparison", createdEmp.getId, emp.getId)
		assertEquals("emp id comparison", createdEmp.getFirstName, emp.getFirstName)
		assertEquals("emp id comparison", createdEmp.getLastName, emp.getLastName)
		assertEquals("emp id comparison", createdEmp.getPhoneNumber, emp.getPhoneNumber)
	}

	@Test
	def getAllRecords : Unit = {
		val empList:java.util.List[Employee]  = employeeService.getAllEmployees
		assertNotNull(empList)
		assert(empList.size() > 0)
	}

	@Test
	def deleteRecord: Unit = {
		val result = employeeService.deleteEmployee(200)
		assertTrue(result)
	}

	@Test
	def getRecordByEmail: Unit = {
		val emp = employeeService.getEmployeeByEmailId("venkatram@hotmail.com")
		assertNotNull(emp)
		assert( emp.getId > 0)
	}

	@Test
	def bulkTest: Unit = {
		val start = System.currentTimeMillis()
		employeeService.bulkCreate(1000000)
		var timeTakenInSeconds  = (System.currentTimeMillis() - start)/1000
		println("Time taken for 1000000 records is "+ timeTakenInSeconds + "secs")
	}

}
