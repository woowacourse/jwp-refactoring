package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderResponses;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;

class OrderServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("주문 생성 실패 - 주문 항목 없음")
    @Test
    void createFail_When_OrderLineItems_Not_Exist() {
        OrderRequest orderRequest = new OrderRequest(1L, null);

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 존재하지 않는 주문 테이블")
    @Test
    void createFail_When_Invalid_OrderTable() {
        OrderRequest orderRequest = new OrderRequest(-1L, Lists.newArrayList());

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 비어있는 테이블")
    @Test
    void createFail_When_OrderTable_IsEmpty() {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 3, true));

        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), Lists.newArrayList());

        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 3, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("피자류"));
        Menu savedMenu = menuRepository.save(new Menu("피자", BigDecimal.valueOf(13_000L), menuGroup));

        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(),
            Lists.newArrayList(new OrderLineItemRequest(savedMenu.getId(), 1)));
        OrderResponse orderResponse = orderService.create(orderRequest);

        assertAll(
            () -> assertThat(orderResponse.getId()).isNotNull(),
            () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(savedOrderTable.getId()),
            () -> assertThat(orderResponse.getOrderLineItems().size()).isEqualTo(1)
        );
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 3, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("피자류"));
        Menu savedMenu = menuRepository.save(new Menu("피자", BigDecimal.valueOf(13_000L), menuGroup));
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(),
            Lists.newArrayList(new OrderLineItemRequest(savedMenu.getId(), 1)));
        OrderResponse savedOrder = orderService.create(orderRequest);

        OrderResponses orderResponses = orderService.list();
        List<OrderResponse> orders = orderResponses.getOrders();

        assertAll(
            () -> assertThat(orders).hasSize(1),
            () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
            () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(savedOrder.getOrderStatus()),
            () -> assertThat(orders.get(0).getId()).isEqualTo(savedOrder.getId())
        );
    }

    @DisplayName("주문 변경 실패 - 존재하지 않는 주문")
    @Test
    void changeOrderStatusFail_When_Order_Not_Exist() {
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderStatusChangeRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 변경")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING", "COMPLETION"})
    void changeOrderStatus(OrderStatus orderStatus) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, false));
        Order order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now()));

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(orderStatus.name());

        orderService.changeOrderStatus(order.getId(), orderStatusChangeRequest);
        Order changedOrder = orderRepository.findById(order.getId())
            .orElseThrow(RuntimeException::new);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatus);
    }
}
