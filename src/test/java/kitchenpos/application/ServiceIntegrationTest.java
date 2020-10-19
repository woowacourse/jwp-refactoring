package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.config.ServiceIntegrationTestConfig;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;

@SpringBootTest
@Import(ServiceIntegrationTestConfig.class)
@Transactional
@Ignore
class ServiceIntegrationTest {
    static final String ORDER_STATUS_COOKING = "COOKING";
    private static final int PRICE_SCALE = 2;

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
        for (int i = 0; i < PRICE_SCALE; i ++) {
            price *= 10;
        }
        return price;
    }
}
