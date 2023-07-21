package com.bestdevelloperarchi.revurestapi.configuration;


import com.bestdevelloperarchi.revurestapi.entities.Employee;
import com.bestdevelloperarchi.revurestapi.repositories.EmployeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(EmployeRepository employeRepository){
        Employee employee1 = Employee.builder()
                .name("Bilbo Baggins")
                .role("burglar")
                .mobileNo("9788585662")
                .emailId("abdi@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .name("Frodo Baggins")
                .role("thief")
                .mobileNo("9656352542")
                .emailId("martelluiz125@gmail.com")
                .build();

        return args -> {
            log.info("Preloading "+employeRepository.save(employee1));
            log.info("Preloading "+employeRepository.save(employee2));
        };
    }
}
