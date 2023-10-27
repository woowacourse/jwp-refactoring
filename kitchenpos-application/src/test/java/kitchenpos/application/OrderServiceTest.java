package kitchenpos.application;

import kitchenpos.domain.dto.OrderRequest;
import kitchenpos.domain.dto.OrderRequest.OrderLineItemRequest;
import kitchenpos.domain.dto.OrderResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private MenuRepository menuRepository;

    private OrderLineItems orderLineItems;

    @BeforeEach
    void setUp() {
        final Menu menu1 = menuRepository.findById(1L).get();
        final Menu menu2 = menuRepository.findById(2L).get();

        final OrderLineItem orderLineItem1 = new OrderLineItem(menu1.getId(), 1L, menu1.getName(), menu1.getPrice());
        final OrderLineItem orderLineItem2 = new OrderLineItem(menu2.getId(), 2L, menu2.getName(), menu2.getPrice());

        orderLineItems = new OrderLineItems(List.of(orderLineItem1, orderLineItem2));
    }

    @Test
    @DisplayName("모든 주문 목록을 조회한다.")
    void listTest() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0));


        orderRepository.save(new Order(orderTable, orderLineItems));

        final List<Order> orders = orderRepository.findAll();
        final List<Long> expect = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        // when
        final List<OrderResponse> orderResponses = orderService.list();
        final List<Long> actual = orderResponses.stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Nested
    @DisplayName("주문을 생성한다.")
    class CreateTest {

        @Test
        @DisplayName("생성된 주문의 상태는 COOKING이다.")
        void createOrderTest() {
            // given
            final OrderTable table = orderTableRepository.save(new OrderTable(1));

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);

            final OrderRequest request = new OrderRequest(table.getId(), null, List.of(orderLineItemRequest));

            // when
            final OrderResponse response = orderService.create(request);

            // then
            Assertions.assertEquals(OrderStatus.COOKING.name(), response.getOrderStatus());
        }

        @Test
        @DisplayName("OrderLineItem이 비어있을 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_OrderLineItem_is_empty() {
            // given
            final OrderTable table = orderTableRepository.save(new OrderTable(0));

            final OrderRequest request = new OrderRequest(table.getId(), null, Collections.emptyList());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> orderService.create(request));
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_OrderTable_is_not_saved() {
            // given
            final OrderTable notSavedOrderTable = new OrderTable(0);

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);

            final OrderRequest request = new OrderRequest(notSavedOrderTable.getId(), null, List.of(orderLineItemRequest));

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> orderService.create(request));
        }

        @Test
        @DisplayName("주문 테이블이 비어있는 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_OrderTable_is_empty() {
            // given
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);

            final OrderRequest request = new OrderRequest(null, null, List.of(orderLineItemRequest));

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> orderService.create(request));
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class ChangeOrderStatusTest {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
        @DisplayName("주문 상태를 변경한다.")
        void changeOrderStatusTest(final OrderStatus orderStatus) {
            // given
            final OrderTable table = orderTableRepository.save(new OrderTable(0));

            final Order order = orderRepository.save(new Order(table, orderLineItems));

            final OrderRequest request = new OrderRequest(null, orderStatus, null);

            // when
            orderService.changeOrderStatus(order.getId(), request);

            // then
            orderRepository.findById(order.getId())
                    .ifPresentOrElse(
                            actual -> Assertions.assertEquals(orderStatus, actual.getOrderStatus()),
                            () -> fail("Order가 존재하지 않습니다.")
                    );
        }

        @Test
        @DisplayName("완료된 주문의 상태를 변경할 경우 IllegalArgumentException이 발생한다.")
        void should_throw_when_change_orderStatus_completion() {
            // given
            final OrderTable table = orderTableRepository.save(new OrderTable(0));

            final Order order = new Order(table, orderLineItems);
            order.updateOrderStatus(OrderStatus.COMPLETION);
            orderRepository.save(order);

            final OrderRequest request = new OrderRequest(null, OrderStatus.MEAL, null);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> orderService.changeOrderStatus(order.getId(), request));
        }
    }
}
