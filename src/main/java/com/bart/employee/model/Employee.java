package com.bart.employee.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.util.Objects;

@Entity
@NamedQuery(name = "Employee.findAllActiveEmployees",
        query = "SELECT e FROM Employee e WHERE e.activityDetails.id IS NOT NULL")
@SequenceGenerator(name = "employeeIdSequence", sequenceName = "employeeIdSequence", allocationSize = 1)
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeIdSequence")
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private String name;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ActiveEmployee activityDetails;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ActiveEmployee getActivityDetails() {
        return activityDetails;
    }
    
    public void setActivityDetails(ActiveEmployee activityDetails) {
        this.activityDetails = activityDetails;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(activityDetails, employee.activityDetails);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, activityDetails);
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", activityDetails=" + activityDetails +
                '}';
    }
}
