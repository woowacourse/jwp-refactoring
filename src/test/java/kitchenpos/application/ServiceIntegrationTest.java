package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.config.ServiceIntegrationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@SpringBootTest
@Import(ServiceIntegrationTestConfig.class)
@Transactional
@Ignore
class ServiceIntegrationTest {
    static final String ORDER_STATUS_COOKING = "COOKING";
    private static final int PRICE_SCALE = 2;

    static MenuProduct getMenuProduct(long quantity, Long id) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(quantity);
        menuProduct.setProductId(id);
        return menuProduct;
    }

    static Menu getMenu(String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return getMenu(name, BigDecimal.valueOf(price, PRICE_SCALE), menuGroupId, menuProducts);
    }

    static Menu getMenuWithNullPrice(String name, Long menuGroupId, List<MenuProduct> menuProducts) {
        return getMenu(name, null, menuGroupId, menuProducts);
    }

    private static Menu getMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    static MenuGroup getMenuGroupWithoutId(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    static MenuGroup getMenuGroupWithId(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    static Order getOrder(Long tableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(2L);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    static OrderLineItem getOrderLineItem(Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    static Order getOrderWithCookingStatus() {
        Order order = new Order();
        order.setOrderStatus(ORDER_STATUS_COOKING);
        return order;
    }

    static Product getProductWithoutId(String name, long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(unscaledPrice(price), PRICE_SCALE));
        return product;
    }

    static Product getProductWithId(Long id, String name, long price) {
        Product product = getProductWithoutId(name, price);
        product.setId(id);
        return product;
    }

    private static long unscaledPrice(long price) {
        for (int i = 0; i < PRICE_SCALE; i++) {
            price *= 10;
        }
        return price;
    }

    static OrderTable getNotEmptyOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        return orderTable;
    }

    static OrderTable getOrderTableWithGuests(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    static List<OrderTable> getOrderTablesWithId(Long... ids) {
        return Arrays.stream(ids)
            .map(id -> getOrderTableWithId(id))
            .collect(Collectors.toList());
    }

    private static OrderTable getOrderTableWithId(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        return orderTable;
    }
}
