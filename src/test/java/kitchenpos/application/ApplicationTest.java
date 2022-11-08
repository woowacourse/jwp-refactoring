package kitchenpos.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
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
