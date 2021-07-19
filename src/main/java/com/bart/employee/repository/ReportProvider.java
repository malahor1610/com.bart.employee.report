package com.bart.employee.repository;

public interface ReportProvider {
    void generateReport(boolean active) throws Exception;
}
