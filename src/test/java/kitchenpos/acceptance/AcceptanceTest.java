package kitchenpos.acceptance;

import kitchenpos.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:/database-cleanup.sql")
public abstract class AcceptanceTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
