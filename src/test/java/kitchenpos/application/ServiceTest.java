package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.*;
import kitchenpos.repository.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static kitchenpos.DomainFactory.*;

@SpringBootTest
public class ServiceTest {
    protected MenuGroup saveMenuGroup(MenuGroupRepository menuGroupRepository, String name) {
        MenuGroup menuGroup = createMenuGroup(name);
        return menuGroupRepository.save(menuGroup);
    }

    protected Product saveProduct(ProductRepository productRepository, String name, BigDecimal price) {
        Product product = createProduct(name, price);
        return productRepository.save(product);
    }

    protected Menu saveMenu(MenuRepository menuRepository, Long menuGroupId, String name, BigDecimal price) {
        Menu menu = createMenu(menuGroupId, name, price);
        return menuRepository.save(menu);
    }

    protected MenuProduct saveMenuProduct(MenuProductRepository menuProductRepository, Long menuId, Long productId,
                                          long quantity) {
        MenuProduct menuProduct = createMenuProduct(menuId, productId, quantity);
        return menuProductRepository.save(menuProduct);
    }

    protected OrderTable saveOrderTable(OrderTableRepository orderTableRepository, int numberOfGuests, boolean empty) {
        return saveOrderTable(orderTableRepository, numberOfGuests, empty, null);
    }

    protected OrderTable saveOrderTable(OrderTableRepository orderTableRepository, int numberOfGuests,
                                        boolean empty, Long tableGroupId) {
        OrderTable orderTable = createOrderTable(numberOfGuests, empty, tableGroupId);
        return orderTableRepository.save(orderTable);
    }

    protected Order saveOrder(OrderRepository orderRepository, Long orderTableId, OrderStatus orderStatus,
                              LocalDateTime orderedTime) {
        Order order = createOrder(orderTableId, orderStatus, orderedTime);
        return orderRepository.save(order);
    }

    protected OrderLineItem saveOrderLineItem(OrderLineItemRepository orderLineItemRepository, Long orderId,
                                              Long menuId, long quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(orderId, menuId, quantity);
        return orderLineItemRepository.save(orderLineItem);
    }

    protected TableGroup saveTableGroup(TableGroupRepository tableGroupRepository) {
        TableGroup tableGroup = createTableGroup();
        return tableGroupRepository.save(tableGroup);
    }
}
