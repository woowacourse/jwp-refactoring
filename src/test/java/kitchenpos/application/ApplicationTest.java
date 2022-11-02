package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("classpath:truncate.sql")
public class ApplicationTest {

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected ProductDao productDao;

    protected Long 메뉴_생성(Menu menu) {
        return menuDao.save(menu);
    }

    protected Long 메뉴그룹_생성(MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Long 주문테이블_생성(OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    protected Long 단체지정_생성(TableGroup tableGroup) {
        return tableGroupDao.save(tableGroup);
    }

    protected Long 상품_생성(Product product) {
        return productDao.save(product);
    }

    protected Long 주문_생성(Order order) {
        return orderDao.save(order);
    }
}
