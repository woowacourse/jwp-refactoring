package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    @Nested
    class 주문_등록_메소드는 extends ServiceTest {

        @Test
        void 입력받은_주문을_저장한다() {
            // given
            OrderTable orderTable = 테이블을_저장한다(4);
            Menu menu = 메뉴를_저장한다("메뉴");

            OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menu.getId(), 3L);
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItemRequest));

            // when
            OrderResponse response = orderService.create(request);

            // then
            assertAll(() -> {
                assertThat(response.getId()).isNotNull();
                assertThat(response)
                        .extracting(OrderResponse::getOrderTableId, OrderResponse::getOrderStatus)
                        .containsExactly(request.getOrderTableId(), OrderStatus.COOKING.name());
            });
        }

        @Test
        void 메뉴_목록이_비어있다면_예외가_발생한다() {
            // given
            OrderTable orderTable = 테이블을_저장한다(4);
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴를_주문하면_예외가_발생한다() {
            // given
            OrderTable orderTable = 테이블을_저장한다(4);

            OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(0L, 3L);
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문하는_테이블이_존재하지_않는다면_예외가_발생한다() {
            // given
            Menu menu = 메뉴를_저장한다("메뉴");

            OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menu.getId(), 3L);
            OrderCreateRequest request = new OrderCreateRequest(0L, List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문하는_테이블이_비어_있다면_예외가_발생한다() {
            // given
            OrderTable orderTable = 빈_테이블을_저장한다();
            Menu menu = 메뉴를_저장한다("메뉴");

            OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menu.getId(), 3L);
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문_목록_조회_메소드는_모든_주문을_조회한다() {
        // given
        Order savedOrder1 = 주문을_저장한다();
        Order savedOrder2 = 주문을_저장한다();

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders)
                .extracting(OrderResponse::getId, OrderResponse::getOrderStatus)
                .contains(tuple(savedOrder1.getId(), OrderStatus.COOKING.name()),
                        tuple(savedOrder2.getId(), OrderStatus.COOKING.name()));
    }

    @Nested
    class 주문_상태_변경_메소드는 extends ServiceTest {

        @Test
        void 주문_상태를_변경한다() {
            // given
            Order savedOrder = 주문을_저장한다();
            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());

            // when
            orderService.changeOrderStatus(savedOrder.getId(), request);

            // then
            Order order = orderDao.findById(savedOrder.getId()).get();
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 주문_완료_상태인_주문을_변경하려고_하면_예외가_발생한다() {
            // given
            Order savedOrder = 주문을_저장한다();

            Order order = new Order(savedOrder.getId(), savedOrder.getOrderTableId(), OrderStatus.COMPLETION.name(),
                    savedOrder.getOrderedTime(), savedOrder.getOrderLineItems());
            orderDao.save(order);

            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
