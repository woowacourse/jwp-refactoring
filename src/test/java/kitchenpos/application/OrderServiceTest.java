package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.order.CreateOrderCommand;
import kitchenpos.application.dto.order.CreateOrderResponse;
import kitchenpos.application.dto.orderlineitem.OrderLineItemCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationTest {

    @Test
    void 주문_항목이_null이면_예외가_발생한다() {
        // given
        CreateOrderCommand command = new CreateOrderCommand(null, null);

        // when & then
        assertThatThrownBy(() -> orderService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목이_없으면_예외가_발생한다() {
        // given
        CreateOrderCommand command = new CreateOrderCommand(null, List.of());

        // when & then
        assertThatThrownBy(() -> orderService.create(command))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 존재하지_않는_메뉴를_주문하면_예외가_발생한다() {
        // given
        OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(1L, 0);
        CreateOrderCommand command = new CreateOrderCommand(null, List.of(orderLineItemCommand));

        // when & then
        assertThatThrownBy(() -> orderService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 메뉴가_있는경우 {

        private Order order;

        @BeforeEach
        void setUp() {
            OrderLineItem orderLineItem = new OrderLineItem(맛있는_메뉴(), 0);
            order = new Order(null, null, null, null, List.of(orderLineItem));
        }

        @Test
        void 존재하지_않는_주문테이블로_주문하면_예외가_발생한다() {
            // given
            Menu 맛있는_메뉴 = 맛있는_메뉴();
            OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(맛있는_메뉴.id(), 0);
            CreateOrderCommand command = new CreateOrderCommand(1L, List.of(orderLineItemCommand));

            // when & then
            assertThatThrownBy(() -> orderService.create(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            Menu 맛있는_메뉴 = 맛있는_메뉴();
            OrderTable 주문_테이블 = 주문_테이블(true);

            OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(맛있는_메뉴.id(), 0);
            CreateOrderCommand command = new CreateOrderCommand(주문_테이블.id(), List.of(orderLineItemCommand));

            // when & then
            assertThatThrownBy(() -> orderService.create(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Nested
        class 주문_테이블이_비어있지_않은_경우 {

            @Test
            void 주문을_저장한다() {
                // given
                Menu 맛있는_메뉴 = 맛있는_메뉴();
                OrderTable 주문_테이블 = 주문_테이블(false);

                OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(맛있는_메뉴.id(), 0);
                CreateOrderCommand command = new CreateOrderCommand(주문_테이블.id(), List.of(orderLineItemCommand));

                // when
                CreateOrderResponse result = orderService.create(command);

                // then
                assertAll(
                        () -> assertThat(result.id()).isPositive(),
                        () -> assertThat(result.orderLineItemResponses().get(0).orderId()).isEqualTo(result.id())
                );
            }

            @Test
            void 주문들을_조회한다() {
                // given
                Order order1 = 맛있는_메뉴_주문();
                Order order2 = 맛있는_메뉴_주문();

                // when
                List<Order> result = orderService.list();

                // then
                assertAll(
                        () -> assertThat(result).hasSize(2),
                        () -> assertThat(result.get(0).id()).isEqualTo(order1.id()),
                        () -> assertThat(result.get(1).id()).isEqualTo(order2.id())
                );
            }

            @Test
            void 주문이_존재하지_않으면_예외가_발생한다() {
                // when & then
                assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 완료된_주문의_상태를_변경하면_예외가_발생한다() {
                // given
                Order order = 완료된_주문();
                Order 맛있는_메뉴_주문 = 맛있는_메뉴_주문();

                // when & then
                assertThatThrownBy(() -> orderService.changeOrderStatus(order.id(), 맛있는_메뉴_주문))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 주문의_상태를_변경한다() {
                // given
                Order order = 맛있는_메뉴_주문();
                Order 식사중인_주문 = 식사중인_주문();

                // when
                Order result = orderService.changeOrderStatus(order.id(), 식사중인_주문);

                // then
                assertThat(result.orderStatus()).isEqualTo(OrderStatus.MEAL.name());
            }
        }
    }
}
