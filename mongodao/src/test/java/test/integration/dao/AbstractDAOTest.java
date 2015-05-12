package test.integration.dao;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.util.Random;

@ContextConfiguration ("/test/integration/dao/daoTestContext.xml")
public class AbstractDAOTest extends AbstractTestNGSpringContextTests {
    protected static final Random RANDOM = new Random();

}
