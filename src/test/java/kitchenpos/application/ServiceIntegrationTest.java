package kitchenpos.application;

import java.util.List;

import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.config.ServiceIntegrationTestConfig;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@SpringBootTest
@Import(ServiceIntegrationTestConfig.class)
@Transactional
@Ignore
class ServiceIntegrationTest {
    static final String ORDER_STATUS_COOKING = "COOKING";

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

}
