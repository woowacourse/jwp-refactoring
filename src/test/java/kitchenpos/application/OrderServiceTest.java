package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemCreateRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ServiceTestConfig {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);
    }

    @DisplayName("주문 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable orderTable = saveOccupiedOrderTable();
            final Menu menu = saveMenu(saveMenuGroup(), saveProduct());
            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 1L);
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItemCreateRequest));

            // when
            final OrderResponse actual = orderService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
                softly.assertThat(actual.getOrderTableId()).isEqualTo(request.getOrderTableId());
                softly.assertThat(actual.getOrderLineItems().size()).isEqualTo(request.getOrderLineItems().size());
                softly.assertThat(actual.getOrderLineItems().get(0).getMenuId()).isEqualTo(request.getOrderLineItems().get(0).getMenuId());
            });
        }

        @DisplayName("OrderLineItems 가 비어있으면 실패한다.")
        @Test
        void fail_if_OrderLineItems_is_empty() {
            // given
            final OrderTable orderTable = saveOccupiedOrderTable();
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderLineItems 에 존재하지 않는 메뉴가 있으면 실패한다.")
        @Test
        void fail_if_not_exist_menu_in_orderLineItems() {
            // given
            final OrderTable orderTable = saveOccupiedOrderTable();
            final Long invalidMenuId = -1L;
            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(invalidMenuId, 2L);
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItemCreateRequest));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 OrderTableId 이면 실패한다.")
        @Test
        void fail_if_invalid_orderTableId() {
            // given
            final Long invalidOrderTableId = -1L;
            final Menu menu = saveMenu(saveMenuGroup(), saveProduct());
            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 1L);
            final OrderCreateRequest request = new OrderCreateRequest(invalidOrderTableId, List.of(orderLineItemCreateRequest));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOfAny(IllegalArgumentException.class, InvalidDataAccessApiUsageException.class);
        }

        @DisplayName("OrderTable 이 빈 테이블이면 실패한다.")
        @Test
        void fail_if_orderTable_is_empty() {
            // given
            final OrderTable orderTable = saveEmptyOrderTable();
            final Menu menu = saveMenu(saveMenuGroup(), saveProduct());
            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 1L);
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItemCreateRequest));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 전체 조회")
    @Nested
    class ReadAll {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable orderTable = saveOccupiedOrderTable();
            final Order savedOrder = saveOrder(orderTable);

            // when
            final List<OrderResponse> actual = orderService.list();

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(savedOrder.getId());
            });
        }
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {
        @DisplayName("성공한다.")
        @Test
        void success() {
            //given
            final OrderTable orderTable = saveOccupiedOrderTable();
            final Order order = saveOrder(orderTable);
            final OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.MEAL);

            // when
            final OrderResponse actual = orderService.changeOrderStatus(order.getId(), request);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(request.getOrderStatus());
        }

        @DisplayName("존재하지 않는 Order 이면 실패한다.")
        @Test
        void fail_if_invalid_order_id() {
            //given
            final long invalidOrderId = -1L;
            final OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.MEAL);

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, request))
                    .isInstanceOfAny(IllegalArgumentException.class, InvalidDataAccessApiUsageException.class);
        }

        @DisplayName("완료된 상태의 Order 이면 실패한다.")
        @Test
        void fail_if_order_status_of_original_order_is_complete() {
            //given
            final OrderTable orderTable = saveOccupiedOrderTable();
            final Order order = saveOrder(orderTable);
            order.changeStatus(OrderStatus.COMPLETION);
            final OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.MEAL);

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
