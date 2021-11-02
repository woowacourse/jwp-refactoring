package kitchenpos.acceptance;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
public abstract class AcceptanceTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;
}
