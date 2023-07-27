package com.example.demo.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@XmlRootElement
@XmlType(propOrder = { "id", "firstName", "lastName", "emailId", "age", "salary" })
public class Employee {
	private Integer id;
	private String firstName;
	private String lastName;
	private String emailId;
	private Integer age;
	private Double salary;
}
