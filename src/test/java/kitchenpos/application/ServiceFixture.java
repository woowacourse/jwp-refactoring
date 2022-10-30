package kitchenpos.application;

import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.OrderLineItemFixture.ORDER_LINE_ITEM_1;

import java.time.LocalDateTime;
import java.util.List;
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
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@SuppressWarnings("NonAsciiCharacters")
public class ServiceFixture {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuProductDao menuProductDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;

    protected Product 제품을_저장한다(final Product product) {
        return productDao.save(product);
    }

    protected MenuGroup 메뉴그룹을_저장한다(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Menu 메뉴를_저장한다(final Menu menu) {
        return menuDao.save(menu);
    }

    protected MenuProduct 메뉴상품을_저장한다(final MenuProduct menuProduct) {
        return menuProductDao.save(menuProduct);
    }

    protected TableGroup 테이블그룹을_저장한다(final TableGroup tableGroup) {
        return tableGroupDao.save(tableGroup);
    }

    protected OrderTable 주문테이블을_저장한다(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    protected Order 주문을_저장한다(final Order order) {
        return orderDao.save(order);
    }

    protected Order 주문항목과_함께_주문을_저장한다(final Long orderTableId, final OrderStatus orderStatus) {
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu menu = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId));
        final List<OrderLineItem> orderLineItems = List.of(ORDER_LINE_ITEM_1.생성(menu));
        return 주문을_저장한다(new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems));
    }

    protected OrderLineItem 주문항목을_저장한다(final OrderLineItem orderLineItem) {
        return orderLineItemDao.save(orderLineItem);
    }
}
