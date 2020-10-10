package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;

@Sql("/truncate.sql")
@SpringBootTest
public class ServiceTest {

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderDao orderDao;
}
