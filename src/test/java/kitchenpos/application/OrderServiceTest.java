package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import kitchenpos.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("새로운 주문 추가")
    @Test
    void createTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Menu savedMenu = saveMenu(menuRepository, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        OrderTable savedOrderTable = saveOrderTable(orderTableRepository, 4, false);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 1L);
        OrderRequest orderRequest =
                new OrderRequest(savedOrderTable.getId(), Collections.singletonList(orderLineItemRequest));

        OrderResponse orderResponse = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(orderResponse.getOrderLineItems()).hasSize(1),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orderResponse.getOrderedTime()).isBefore(LocalDateTime.now())
        );
    }

    @DisplayName("새로운 주문에 주문 항목이 없을 때 예외 반환")
    @Test
    void createOrderWithEmptyOrderLineItemTest() {
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문의 주문 항목에 잘못된 메뉴 선택시 예외 반환")
    @Test
    void createOrderWithInvalidOrderLineITemTest() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(0L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 등록 시 존재하지 않는 주문 테이블 번호 입력 시 예외 반환")
    @Test
    void createOrderWithInvalidOrderTableTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Menu savedMenu = saveMenu(menuRepository, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(0L, Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 주문 등록 시 비어있는 테이블에 주문 추가 시 예외 반환")
    @Test
    void createOrderWithEmptyOrderTableTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Menu savedMenu = saveMenu(menuRepository, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        OrderTable savedOrderTable = saveOrderTable(orderTableRepository, 0, true);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 1L);
        OrderRequest orderRequest =
                new OrderRequest(savedOrderTable.getId(), Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> {
            orderService.create(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("저장된 모든 주문 조회")
    @Test
    void listTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Menu savedMenu = saveMenu(menuRepository, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        OrderTable savedOrderTable = saveOrderTable(orderTableRepository, 0, false);
        Order savedOrder = saveOrder(orderRepository, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());
        saveOrderLineItem(orderLineItemRepository, savedOrder.getId(), savedMenu.getId(), 1L);

        List<OrderResponse> orderResponses = orderService.list();

        assertAll(
                () -> assertThat(orderResponses).hasSize(1),
                () -> assertThat(orderResponses.get(0).getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("주문의 상태 변경")
    @Test
    void changeOrderStatusTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Menu savedMenu = saveMenu(menuRepository, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        OrderTable savedOrderTable = saveOrderTable(orderTableRepository, 4, false);
        Order savedOrder = saveOrder(orderRepository, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());
        saveOrderLineItem(orderLineItemRepository, savedOrder.getId(), savedMenu.getId(), 1L);

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.COMPLETION.name());

        OrderResponse orderResponse = orderService.changeOrderStatus(savedOrder.getId(), orderStatusRequest);

        assertAll(
                () -> assertThat(orderResponse.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(savedOrder.getOrderTable().getId()),
                () -> assertThat(orderResponse.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime()),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
                () -> assertThat(orderResponse.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("주문 상태 변경 시 존재하지 않는 주문 입력 시 예외 반환")
    @Test
    void changeOrderStatusWithInvalidOrderTest() {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(0L, orderStatusRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 주문의 상태 변경 시 예외 반환")
    @Test
    void changeOrderStatusWithCompletionOrderTest() {
        OrderTable savedOrderTable = saveOrderTable(orderTableRepository, 0, false);
        Order savedOrder = saveOrder(orderRepository, savedOrderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now());

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(savedOrder.getId(), orderStatusRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderLineItemRepository.deleteAll();
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        menuRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}