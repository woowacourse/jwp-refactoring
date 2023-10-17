package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTableRepository;
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
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;
    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
