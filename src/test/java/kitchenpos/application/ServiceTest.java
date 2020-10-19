package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.repository.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static kitchenpos.DomainFactory.*;

@SpringBootTest
public class ServiceTest {
    protected static final int BIG_DECIMAL_FLOOR_SCALE = 2;

    protected MenuGroup saveMenuGroup(MenuGroupRepository menuGroupRepository, String name) {
        MenuGroup menuGroup = createMenuGroup(name);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return savedMenuGroup;
    }

    protected Product saveProduct(ProductRepository productRepository, String name, BigDecimal price) {
        Product product = createProduct(name, price);
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    protected Menu saveMenu(MenuRepository menuRepository, Long menuGroupId, String name, BigDecimal price) {
        Menu menu = createMenu(menuGroupId, name, price);
        Menu savedMenu = menuRepository.save(menu);
        return savedMenu;
    }

    protected MenuProduct saveMenuProduct(MenuProductRepository menuProductRepository, Long menuId, Long productId,
                                          long quantity) {
        MenuProduct menuProduct = createMenuProduct(menuId, productId, quantity);
        MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
        return savedMenuProduct;
    }

    protected OrderTable saveOrderTable(OrderTableRepository orderTableRepository, int numberOfGuests, boolean empty) {
        return saveOrderTable(orderTableRepository, numberOfGuests, empty, null);
    }

    protected OrderTable saveOrderTable(OrderTableRepository orderTableRepository, int numberOfGuests,
                                        boolean empty, Long tableGroupId) {
        OrderTable orderTable = createOrderTable(numberOfGuests, empty, tableGroupId);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return savedOrderTable;
    }

    protected Order saveOrder(OrderRepository orderRepository, Long orderTableId, String orderStatus,
                              LocalDateTime orderedTime) {
        Order order = createOrder(orderTableId, orderStatus, orderedTime);
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    protected OrderLineItem saveOrderLineItem(OrderLineItemRepository orderLineItemRepository, Long orderId,
                                              Long menuId, long quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(orderId, menuId, quantity);
        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);
        return savedOrderLineItem;
    }

    protected TableGroup saveTableGroup(TableGroupRepository tableGroupRepository) {
        TableGroup tableGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return savedTableGroup;
    }
}
