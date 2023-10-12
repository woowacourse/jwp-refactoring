package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql("/truncate.sql")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public abstract class IntegrationTest {
    @Autowired
    protected MenuDao menuDao;
    @Autowired
    protected MenuGroupDao menuGroupDao;
    @Autowired
    protected MenuProductDao menuProductDao;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected OrderLineItemDao orderLineItemDao;
    @Autowired
    protected OrderTableDao orderTableDao;
    @Autowired
    protected ProductDao productDao;
    @Autowired
    protected TableGroupDao tableGroupDao;
}
