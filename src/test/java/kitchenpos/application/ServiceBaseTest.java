package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/data.sql")
public abstract class ServiceBaseTest {

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
