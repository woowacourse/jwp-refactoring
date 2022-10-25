package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestFixture;
import kitchenpos.application.OrderService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class OrderServiceTest {

    private final OrderService orderService;
    private final OrderTableDao orderTableDao;
    private final TestFixture testFixture;

    OrderTable orderTable;
    Menu menu;

    public OrderServiceTest(OrderService orderService, OrderTableDao orderTableDao, TestFixture testFixture) {
        this.orderService = orderService;
        this.orderTableDao = orderTableDao;
        this.testFixture = testFixture;
    }

    @BeforeEach
    void setUp() {
        orderTable = orderTableDao.save(new OrderTable(100, false));
        menu = testFixture.삼겹살_메뉴();
    }

    @DisplayName("주문 내에 메뉴가 비어있다면 예외가 발생한다.")
    @Test
    public void orderSaveWithOrderLineItemsIsEmpty() {
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Collections.emptyList()
        );

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사전에 저장되어 있지 않은 메뉴가 포함되어 있다면 예외가 발생한다.")
    @Test
    public void orderSaveWithOrderLineItemsIsAlreadySaved() {
        OrderLineItem orderLineItem = new OrderLineItem(-1L, 10L);
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void orderSaveWithOrderTableNotSaved() {
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 10L);
        Order order = new Order(
                -1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 EMPTY 상태인 경우 예외가 발생한다.")
    @Test
    void orderSaveWithOrderTableIsEmpty() {
        orderTable = orderTableDao.save(new OrderTable(100, true));
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 10L);
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 성공적으로 저장되는 경우를 테스트한다")
    @Test
    void orderSave() {
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 10L);
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );

        Order savedOrder = orderService.create(order);
        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("주문 리스트를 출력한다.")
    @Test
    void list() {
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 10L);
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );
        orderService.create(order);

        assertThat(orderService.list()).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 10L);
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
