package kitchenpos.support;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class IntegrationTest {

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
