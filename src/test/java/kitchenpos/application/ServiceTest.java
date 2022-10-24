package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ServiceTest {

    @MockBean(name = "productDao")
    protected ProductDao productDao;

    @MockBean(name = "menuGroupDao")
    protected MenuGroupDao menuGroupDao;

    @MockBean(name = "menuDao")
    protected MenuDao menuDao;

    @MockBean(name = "orderTableDao")
    protected OrderTableDao orderTableDao;

    @MockBean(name = "orderDao")
    protected OrderDao orderDao;

    @MockBean(name = "orderLineItemDao")
    protected OrderLineItemDao orderLineItemDao;

    protected Product 후라이드() {
        return new Product("후라이드", BigDecimal.valueOf(10000));
    }

    protected Menu 후라이드후라이드(final Long menuGroupId) {
        final Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(17000));
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    protected MenuProduct getMenuProduct() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    protected MenuGroup 추천메뉴() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        return menuGroup;
    }

    protected Order getOrder() {
        final Order order = new Order();
        order.setOrderStatus(COOKING.name());
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    protected OrderLineItem getOrderLineItem(final Long orderId) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        return orderLineItem;
    }

    protected TableGroup getTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    protected OrderTable getOrderTable(final Long tableGroupId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }
}
