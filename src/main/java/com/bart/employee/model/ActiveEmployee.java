package com.bart.employee.model;

import com.sun.istack.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.sql.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@SequenceGenerator(name = "detailsIdSequence", sequenceName = "detailsIdSequence", allocationSize = 1)
public class ActiveEmployee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detailsIdSequence")
    private Long id;
    @Column(nullable = false)
    private Double salary;
    @Column(nullable = false)
    private Date startDate;
    @ManyToMany(mappedBy = "employees", cascade = CascadeType.PERSIST)
    private Set<Team> teams;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getSalary() {
        return salary;
    }
    
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Set<Team> getTeams() {
        return teams;
    }
    
    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActiveEmployee)) return false;
        ActiveEmployee that = (ActiveEmployee) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(salary, that.salary) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(teams, that.teams);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, salary, startDate, teams);
    }
    
    @Override
    public String toString() {
        return "ActiveEmployee{" +
                "id=" + id +
                ", salary=" + salary +
                ", startDate=" + startDate +
                ", teams=" + teams +
                '}';
    }
}
