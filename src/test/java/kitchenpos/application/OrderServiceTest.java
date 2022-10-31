package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(false)
                    .build());
            private final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTable.getId(),
                    List.of(new OrderLineItemRequest(1L, 1)));

            @Test
            void 주문을_추가한다() {
                OrderResponse actual = orderService.create(orderCreateRequest);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
                    assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                    assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now());
                    assertThat(actual.getOrderLineItems()).hasSize(1);
                });
            }
        }

        @Nested
        class 주문_항목이_비어있을_경우 {

            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(false)
                    .build());
            private final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTable.getId(),
                    List.of());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 항목은 비어있을 수 없습니다.");
            }
        }

        @Nested
        class 주문_항목에_중복되는_메뉴가_있을_경우 {

            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(false)
                    .build());
            private final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTable.getId(),
                    List.of(new OrderLineItemRequest(1L, 1),
                            new OrderLineItemRequest(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 항목엔 중복되는 메뉴가 존재할 수 없습니다.");
            }
        }

        @Nested
        class 주문_항목에_존재하지_않는_메뉴가_있을_경우 {

            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(false)
                    .build());
            private final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTable.getId(),
                    List.of(new OrderLineItemRequest(0L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴가 존재하지 않습니다.");
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않을_경우 {

            private final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    0L,
                    List.of(new OrderLineItemRequest(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 주문_테이블이_비활성화된_경우 {

            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTable.getId(),
                    List.of(new OrderLineItemRequest(1L, 1)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("비활성화된 주문 테이블은 주문을 받을 수 없습니다.");
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final Order order = orderRepository.save(Order.builder()
                    .orderTable(OrderTable.builder()
                            .numberOfGuests(2)
                            .empty(false)
                            .build())
                    .orderStatus(OrderStatus.COOKING)
                    .orderedTime(LocalDateTime.now())
                    .orderLineItems(new OrderLineItems(List.of(OrderLineItem.builder()
                            .menu(menuRepository.findById(1L).orElseThrow())
                            .quantity(1L)
                            .build())))
                    .build());

            @Test
            void 주문_목록을_반환한다() {
                List<OrderResponse> orderResponses = orderService.list();

                assertThat(orderResponses).isNotEmpty();
            }
        }
    }

    @Nested
    class changeOrderStatus_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final Order order = orderRepository.save(Order.builder()
                    .orderTable(OrderTable.builder()
                            .numberOfGuests(2)
                            .empty(false)
                            .build())
                    .orderStatus(OrderStatus.COOKING)
                    .orderedTime(LocalDateTime.now())
                    .orderLineItems(new OrderLineItems(List.of(OrderLineItem.builder()
                            .menu(menuRepository.findById(1L).orElseThrow())
                            .quantity(1L)
                            .build())))
                    .build());
            private final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(
                    OrderStatus.MEAL.name());

            @Test
            void 주문_상태를_변경한다() {
                OrderResponse actual = orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest);

                assertAll(() -> {
                    assertThat(actual.getId()).isEqualTo(order.getId());
                    assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
                });
            }
        }

        @Nested
        class 주문이_존재하지_않을_경우 {

            private final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(
                    OrderStatus.MEAL.name());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(0L, changeOrderStatusRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문이 존재하지 않습니다.");
            }
        }

        @Nested
        class 주문이_이미_계산_완료_상태인_경우 {

            private final Order order = orderRepository.save(Order.builder()
                    .orderTable(OrderTable.builder()
                            .numberOfGuests(2)
                            .empty(false)
                            .build())
                    .orderStatus(OrderStatus.COMPLETION)
                    .orderedTime(LocalDateTime.now())
                    .orderLineItems(new OrderLineItems(List.of(OrderLineItem.builder()
                            .menu(menuRepository.findById(1L).orElseThrow())
                            .quantity(1L)
                            .build())))
                    .build());
            private final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(
                    OrderStatus.MEAL.name());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문이 이미 계산 완료되었습니다.");
            }
        }
    }
}
