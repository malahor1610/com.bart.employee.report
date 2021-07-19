package com.bart.employee.repository;

import com.bart.employee.model.Employee;
import com.bart.employee.utility.DateSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@Component
public class ReportProviderImpl implements ReportProvider {
    
    private static final Path OUTPUT_PDF_FILE_NAME = Paths.get("report.pdf");
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private XmlMapper xmlMapper;
    private Resource xslResource;
    private FopFactory fopFactory;
    private FOUserAgent foUserAgent;
    private TransformerFactory transformerFactory;
    
    @PostConstruct
    private void initialize() throws IOException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.addSerializer(Date.class, new DateSerializer());
        xmlMapper = new XmlMapper(module);
        xslResource = new ClassPathResource("report.xsl");
        fopFactory = FopFactory.newInstance(new File(".").toURI());
        foUserAgent = fopFactory.newFOUserAgent();
        transformerFactory = TransformerFactory.newInstance();
    }
    
    @Override
    public void generateReport(boolean active) throws Exception {
        String xmlData = this.prepareXmlData(active);
        try (OutputStream outputPdfStream = Files.newOutputStream(OUTPUT_PDF_FILE_NAME);
             InputStream inputXslStream = xslResource.getInputStream()) {
            Source src = new StreamSource(new StringReader(xmlData));
            
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputPdfStream);
            Result res = new SAXResult(fop.getDefaultHandler());
            
            Transformer xsltTransformer = transformerFactory.newTransformer(new StreamSource(inputXslStream));
            xsltTransformer.transform(src, res);
        } catch (IOException e) {
            throw new Exception("XSL to PDF transformation failed with error " + e.getMessage(), e.getCause());
        }
    }
    
    private String prepareXmlData(boolean active) throws Exception {
        List<Employee> employees;
        if (active) {
            employees = employeeRepository.findAllActiveEmployees();
        } else {
            employees = employeeRepository.findAll();
        }
        if (employees == null || employees.isEmpty()) {
            throw new Exception("No employees found");
        }
        try {
            return xmlMapper.writeValueAsString(employees);
        } catch (JsonProcessingException e) {
            throw new Exception("Mapping employees to xml failed with error " + e.getMessage(), e.getCause());
        }
    }
}
