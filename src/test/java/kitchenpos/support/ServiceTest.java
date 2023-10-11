package kitchenpos.support;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/clean.sql")
public class ServiceTest {

}
