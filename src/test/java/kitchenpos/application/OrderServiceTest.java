package kitchenpos.application;

import static java.util.Collections.emptyList;
import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderFixture.createOrderRequest;
import static kitchenpos.fixture.OrderFixture.createOrderRequestChangeOrderStatus;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemRequest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceTest extends AbstractServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    OrderTable orderTable;

    MenuGroup menuGroup;

    Menu menu;

    @BeforeEach
    void setup() {
        orderTable = orderTableRepository.save(createOrderTable(null, false, 0, null));
        menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
        menu = menuRepository.save(createMenu(null, "메뉴", 0L, menuGroup.getId()));
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        OrderCreateRequest orderCreateRequest = createOrderRequest(
            orderTable.getId(),
            Collections.singletonList(createOrderLineItemRequest(menu.getId(), 1))
        );

        OrderResponse savedOrder = orderService.create(orderCreateRequest);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOrderTableId())
                .isEqualTo(orderCreateRequest.getOrderTableId()),
            () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertThat(savedOrder.getOrderLineItems())
                .usingElementComparatorIgnoringFields("seq", "orderId")
                .isEqualTo(orderCreateRequest.getOrderLineItems())
        );
    }

    @DisplayName("주문 항목이 0개 이하면 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception() {
        OrderCreateRequest orderCreateRequest = createOrderRequest(orderTable.getId(), emptyList());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(orderCreateRequest))
            .withMessage("주문 항목이 존재해야 합니다.");
    }

    @DisplayName("주문 항목과 메뉴의 개수가 같지 않으면 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception2() {
        OrderCreateRequest orderCreateRequest = createOrderRequest(
            orderTable.getId(),
            Arrays.asList(
                createOrderLineItemRequest(menu.getId(), 1),
                createOrderLineItemRequest(menu.getId(), 1)
            )
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(orderCreateRequest))
            .withMessage("주문 항목 개수와 메뉴 개수는 같아야 합니다.");
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception3() {
        OrderCreateRequest orderCreateRequest = createOrderRequest(
            orderTable.getId() + 1,
            Collections.singletonList(createOrderLineItemRequest(menu.getId(), 1))
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(orderCreateRequest));
    }

    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception4() {
        orderTable = orderTableRepository.save(createOrderTable(null, true, 0, null));
        OrderCreateRequest orderCreateRequest = createOrderRequest(
            orderTable.getId(),
            Collections.singletonList(createOrderLineItemRequest(menu.getId(), 1))
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(orderCreateRequest))
            .withMessage("빈 테이블은 주문할 수 없습니다.");
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<OrderResponse> savedOrders = Stream.of(
            orderRepository.save(createOrder(
                null,
                LocalDateTime.now(),
                OrderStatus.COOKING,
                orderTable.getId())
            ),
            orderRepository.save(createOrder(
                null,
                LocalDateTime.now(),
                OrderStatus.COOKING,
                orderTable.getId())
            ),
            orderRepository.save(createOrder(
                null,
                LocalDateTime.now(),
                OrderStatus.COOKING,
                orderTable.getId())
            )
        )
            .map(it -> OrderResponse.of(it, emptyList()))
            .collect(Collectors.toList());

        List<OrderResponse> allOrders = orderService.list();

        assertThat(allOrders).usingElementComparatorIgnoringFields("orderLineItems")
            .containsAll(savedOrders);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        Order order = orderRepository.save(createOrder(
            null,
            LocalDateTime.now(),
            OrderStatus.COOKING,
            orderTable.getId()
        ));
        OrderChangeOrderStatusRequest orderRequest = createOrderRequestChangeOrderStatus(
            OrderStatus.MEAL
        );

        OrderResponse changedOrder = orderService.changeOrderStatus(order.getId(), orderRequest);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
    }

    @DisplayName("완료된 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_throws_exception() {
        Order order = orderRepository.save(createOrder(
            null,
            LocalDateTime.now(),
            OrderStatus.COMPLETION,
            orderTable.getId()
        ));
        OrderChangeOrderStatusRequest orderRequest = createOrderRequestChangeOrderStatus(
            OrderStatus.COOKING
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderRequest));
    }
}
