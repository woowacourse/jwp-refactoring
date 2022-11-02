package kitchenpos.application;

import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
@SuppressWarnings("NonAsciiCharacters")
public class ServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    protected MenuGroup 메뉴_그룹을_저장한다(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Product 상품을_저장한다(final Product product) {
        return productDao.save(product);
    }

    protected Menu 메뉴를_저장한다(final Menu menu) {
        return menuDao.save(menu);
    }

    protected OrderTable 주문_테이블을_저장한다(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    protected Order 주문을_저장한다(final Order order) {
        return orderDao.save(order);
    }

    protected TableGroup 테이블_그룹을_저장한다(final TableGroup tableGroup) {
        return tableGroupDao.save(tableGroup);
    }

    protected List<OrderTable> 주문_테이블_전체를_조회한다() {
        return orderTableDao.findAll();
    }
}
