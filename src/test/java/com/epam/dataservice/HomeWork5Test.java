package com.epam.dataservice;

import com.epam.springboot.AccidentsRestApplication;
import com.epam.springboot.modal.Accidents;
import com.epam.springboot.modal.RoadConditions;
import com.epam.springboot.repository.AccidentRepository;
import com.epam.springboot.repository.AccidentService;
import com.epam.springboot.repository.AccidentServiceImpl;
import com.epam.springboot.repository.RoadConditionRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by bill on 16-5-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccidentsRestApplication.class)   //MockServletContext
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@WebAppConfiguration
@IntegrationTest("server.port:0")
@DatabaseSetup("/sampleData.xml")
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ItemRepositoryIT.DATASET })
public class HomeWork5Test {
    @Autowired
    RoadConditionRepository roadConditionRepository;
    @Autowired
    AccidentRepository repository;

    @Autowired
    AccidentService accidentService;

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    static Logger log = Logger.getLogger(HomeWork5Test.class.getName());
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void init() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        template = new TestRestTemplate();
//        RestAssured.port = port;
    }

    @Test
    public void AcciedentsTest() {
        Accidents acciedent = new Accidents("200901BS70001");
        log.info(acciedent);

    }

    @Test
    public void AccidentRepositoryTest() {
        String testId = "200901BS70004";
        assertThat(repository.count(), equalTo(16L));
        Accidents accidents = repository.findOne(testId);
        log.info(accidents);
        assertThat(accidents.getId(), equalTo(testId));
    }

    @Test
    public void getHello() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString() + "hello/Bill", String.class);
        assertThat(response.getBody(), equalTo("hello, Bill"));
    }

    @Test
    public void getAllAccidentsByRoadConditionTest() {
        RoadConditions roadCondition = roadConditionRepository.findOne(2);
        List<Accidents> accidentsList = repository.findByRoadSurfaceConditions(roadCondition);
        log.info(accidentsList);
        assertThat(accidentsList.size(), equalTo(4));
        assertThat(accidentsList.get(0).getRoadSurfaceConditions().getCode(), equalTo(roadCondition.getCode()));
    }

    @Test
    // Use default countBy query
    public void getAllAccidentsGroupByRoadCondition1Test() {
        List<RoadConditions> roadConditionsList = roadConditionRepository.findAll();
        for (RoadConditions roadCondition : roadConditionsList) {
            log.info(roadCondition.getCode() + " , " + roadCondition.getLabel() + " , Count="
                    + repository.countByRoadSurfaceConditions(roadCondition));
        }
        assertThat(roadConditionsList.size(), equalTo(8));
    }

    @Test
    // Use accident service
    public void getAllAccidentsGroupByRoadCondition2Test() {
        Map<String, Integer> roadConditionsList = accidentService.getAccidentCountGroupByRoadCondition();
        // Java8
        roadConditionsList.forEach((k, v) -> log.info("Road Condition: " + k + ", Count=" + v));
        assertThat(roadConditionsList.get("Dry"), equalTo(11));
    }

    @Test
    public void getAllAccidentsByDateTest() throws ParseException {
        RoadConditions roadCondition = roadConditionRepository.findOne(1);
        List<Accidents> accidentsList = repository.findByRoadSurfaceConditionsAndDateBetween(
                roadCondition,
                simpleDateFormat.parse("2009-01-01"),
                simpleDateFormat.parse("2009-01-10"));
        log.info(accidentsList);
        assertThat(accidentsList.size(), equalTo(8));
    }

    @Test
    public void getAllAccidentsByWeatherConditionAndYearTest() {
        Map<String, Integer> weatherConditionsList = accidentService.getAccidentCountGroupByWeatherConditionAndYear("2009");
        weatherConditionsList.forEach((k, v) -> log.info("Weather Condition: " + k + ", Count=" + v));
        assertThat(weatherConditionsList.get("Fine no high winds"), equalTo(12));
    }

    @Test
    public void updateAccidentByDate() throws ParseException {
        String testDate = "2009-01-07";
        int count = accidentService.updateAccidentTimeByDate(testDate);
        assertThat(count, equalTo(2));
        List<Accidents> accidentsList = repository.findByDate(simpleDateFormat.parse(testDate));
        log.info(accidentsList);
        assertThat(accidentsList.get(0).getTime(), equalTo("AFTERNOON"));
        assertThat(accidentsList.get(1).getTime(), equalTo("NIGHT"));
    }

    @Test
    public void RoadConditionRepositoryTest() {
        assertThat(roadConditionRepository.count(), equalTo(8L));
        List<RoadConditions> roadConditionsList = roadConditionRepository.findAll();
        log.info(roadConditionsList);
        assertThat(roadConditionsList.size(), equalTo(8));
    }

}
/*

Scenarios to be implemented for Homework:


        1. Find all the accidents by ID(Note: We can use findOne method which will accept the Accident ID as PK).

        2. Find all the accidents count groupby all roadsurface conditions .

        3. Find all the accidents count groupby accident year and weather condition .( For eg: in year 2009 we need to know the number of accidents based on each weather condition).

        4. On a given date,  fetch all the accidents and update the Time based on the below rules

        Time Logic:
        MORNING - 6 am to 12 pm
        AFTERNOON - 12 pm to 6 pm
        EVENING - 6 pm to 12 am
        NIGHT - 12 am to 6 am
 */