package kitchenpos.order.repository;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createOrderLineItem;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = OrderTable.builder()
            .empty(false)
            .build();
        Product product = productRepository.save(createProduct(10_000));
        MenuProduct menuProduct = createMenuProduct(product, 2);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
        Menu menu = createMenu(menuProducts,18_000);
        OrderLineItem orderLineItem = createOrderLineItem(menuRepository.save(menu));
        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);
        List<OrderLineItem> orderLineItems = Arrays.asList(savedOrderLineItem);

        order = Order.builder()
            .orderTable(orderTableRepository.save(orderTable))
            .orderLineItems(orderLineItems)
            .build();
    }

    @DisplayName("OrderStatus 변경 시 버전 증가")
    @Test
    void version_OrderStatus() {
        Order savedOrder = orderRepository.save(order);
        savedOrder.changeOrderStatus(OrderStatus.MEAL);
        Order changedOrder = orderRepository.saveAndFlush(savedOrder);

        int actual = changedOrder.getVersion() - savedOrder.getVersion();
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("[예외] 동시에 같은 주문 수정")
    @Test
    void objectOptimisticLockingFailureException() {
        Order savedOrder = orderRepository.save(order);
        savedOrder.changeOrderStatus(OrderStatus.MEAL);
        orderRepository.saveAndFlush(savedOrder);

        assertThatThrownBy(
            () -> {
                savedOrder.changeOrderStatus(OrderStatus.COMPLETION);
                orderRepository.save(savedOrder);
            }
        ).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}