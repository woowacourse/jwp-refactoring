package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    protected Product getProduct(final Long id, final int price) {
        return getProduct(id, "후라이드", price);
    }

    protected Product getProduct() {
        return getProduct(1L, "후라이드", 10000);
    }

    protected Product getProduct(final Long id, final String name, final int price) {
        final Product product = new Product(name, BigDecimal.valueOf(price));
        product.setId(id);
        return product;
    }

    protected Menu getMenu(final int price) {
        return getMenu("후라이드+후라이드", price, 1L, null);
    }

    protected Menu getMenu(final int price, final List<MenuProduct> menuProducts) {
        return getMenu("후라이드+후라이드", price, 1L, menuProducts);
    }

    protected Menu getMenu(final Long menuGroupId) {
        return getMenu("후라이드+후라이드", 17000, menuGroupId, null);
    }

    protected Menu getMenu(final String name,
                           final int price,
                           final Long menuGroupId,
                           final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    protected MenuProduct getMenuProduct() {
        return getMenuProduct(1L, 1L, 1);
    }

    protected MenuProduct getMenuProduct(final Long productId) {
        return getMenuProduct(productId, 1L, 1);
    }

    protected MenuProduct getMenuProduct(final Long productId, final Long menuId, final long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setMenuId(menuId);
        menuProduct.setQuantity(quantity);
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

    protected OrderTable getOrderTable() {
        return getOrderTable(null);
    }

    protected OrderTable getOrderTable(final Long tableGroupId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }
}
