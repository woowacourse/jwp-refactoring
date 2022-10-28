package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
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
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class OrderServiceTest {

    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;
    private final TestFixture testFixture;

    OrderTable orderTable;
    Menu menu;

    public OrderServiceTest(OrderService orderService, OrderTableRepository orderTableRepository,
                            TestFixture testFixture) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
        this.testFixture = testFixture;
    }

    @BeforeEach
    void setUp() {
        orderTable = orderTableRepository.save(new OrderTable(100, false));
        menu = testFixture.삼겹살_메뉴();
    }

    @DisplayName("주문 내에 메뉴가 비어있다면 예외가 발생한다.")
    @Test
    public void orderSaveWithOrderLineItemsIsEmpty() {
        OrderRequest request = new OrderRequest(orderTable.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void orderSaveWithOrderTableNotSaved() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(-1L, 10L);
        OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 EMPTY 상태인 경우 예외가 발생한다.")
    @Test
    void orderSaveWithOrderTableIsEmpty() {
        orderTable = orderTableRepository.save(new OrderTable(100, true));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 10L);
        OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 성공적으로 저장되는 경우를 테스트한다")
    @Test
    void orderSave() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 10L);
        OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
        Order savedOrder = orderService.create(request);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("주문 리스트를 출력한다.")
    @Test
    void list() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 10L);
        OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
        orderService.create(request);

        assertThat(orderService.list()).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeStatus() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 10L);
        OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
        Order order = orderService.create(request);

        Order changedOrder = orderService.changeOrderStatus(order.getId(),
                new OrderStatusRequest("COMPLETION"));

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
