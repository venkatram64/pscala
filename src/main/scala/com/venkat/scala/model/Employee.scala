package com.venkat.scala.model

/**
  * Created by venkatram.veerareddy on 8/28/2017.
  */
public class Employee {

  private var id:Int = 0
  private var firstName:String = _
  private var lastName:String = _
  private var email:String = _
  private var phoneNumber:String = _

  def getId: Int = id

  def setId(id: Int): Unit = {
    this.id = id
  }

  def getFirstName: String = firstName

  def setFirstName(firstName: String): Unit = {
    this.firstName = firstName
  }

  def getLastName: String = lastName

  def setLastName(lastName: String): Unit = {
    this.lastName = lastName
  }

  def getEmail: String = email

  def setEmail(email: String): Unit = {
    this.email = email
  }

  def getPhoneNumber: String = phoneNumber

  def setPhoneNumber(phoneNumber: String): Unit = {
    this.phoneNumber = phoneNumber
  }

}
