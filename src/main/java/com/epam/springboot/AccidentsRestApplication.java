package com.epam.springboot;

import com.epam.springboot.modal.Accidents;
import com.epam.springboot.modal.RoadConditions;
import com.epam.springboot.repository.AccidentRepository;
import com.epam.springboot.repository.AccidentService;
import com.epam.springboot.repository.AccidentServiceImpl;
import com.epam.springboot.repository.RoadConditionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by bill on 16-5-22.
 */

@SpringBootApplication
public class AccidentsRestApplication {
    public static ApplicationContext getContext() {
        return context;
    }

    private static ApplicationContext context;
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication();
        context = SpringApplication.run(AccidentsRestApplication.class, args);
        initDB(context);
    }

    private static void initDB(ApplicationContext ctx){
//        AccidentRepository accidentRepository= ctx.getBean(AccidentRepository.class);
//        RoadConditionRepository roadConditionRepository= context.getBean(RoadConditionRepository.class);
//        roadConditionRepository.save(new RoadConditions(12,"Test Sample"));
//        AccidentServiceImpl accidentService = (AccidentServiceImpl)context.getBean("accidentService");
//        AccidentServiceImpl accidentServiceImpl = context.getBean(AccidentServiceImpl.class);
//        accidentRepository.save(new Accidents("200901BS70001"));
//        accidentRepository.save(new Accidents("200901BS70002",3));
//        List<Accidents> accidentsList = accidentRepository.findAll();
//        System.out.println(accidentsList);
    }
//    @Bean

}
