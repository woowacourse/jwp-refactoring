package kitchenpos.application;

import static kitchenpos.exception.MenuExceptionType.MENU_NOT_FOUND;
import static kitchenpos.exception.OrderExceptionType.CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS;
import static kitchenpos.exception.OrderExceptionType.ORDER_LINE_ITEMS_CAN_NOT_EMPTY;
import static kitchenpos.exception.OrderExceptionType.ORDER_NOT_FOUND;
import static kitchenpos.exception.OrderExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;
import static kitchenpos.exception.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.application.dto.order.ChangeOrderStatusCommand;
import kitchenpos.application.dto.order.ChangeOrderStatusResponse;
import kitchenpos.application.dto.order.CreateOrderCommand;
import kitchenpos.application.dto.order.CreateOrderResponse;
import kitchenpos.application.dto.orderlineitem.OrderLineItemCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationTest {

    @Test
    void 존재하지_않는_주문테이블로_주문하면_예외가_발생한다() {
        // given
        Long noExistOrderTableId = 1L;
        CreateOrderCommand command = new CreateOrderCommand(noExistOrderTableId, List.of());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                orderService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_NOT_FOUND);
    }

    @Nested
    class 주문테이블이_있는경우 {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = 주문_테이블(false);
        }

        @Test
        void 존재하지_않는_메뉴를_주문하면_예외가_발생한다() {
            // given
            Long noExistMenuId = 1L;
            OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(noExistMenuId, 0);
            CreateOrderCommand command = new CreateOrderCommand(orderTable.id(), List.of(orderLineItemCommand));

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    orderService.create(command)
            ).exceptionType();

            // when
            assertThat(exceptionType).isEqualTo(MENU_NOT_FOUND);
        }

        @Test
        void 주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            OrderTable 주문_테이블 = 주문_테이블(true);
            CreateOrderCommand command = new CreateOrderCommand(주문_테이블.id(), List.of());

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    orderService.create(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_TABLE_CAN_NOT_EMPTY);
        }

        @Test
        void 주문_항목이_없으면_예외가_발생한다() {
            // given
            CreateOrderCommand command = new CreateOrderCommand(orderTable.id(), List.of());

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    orderService.create(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_LINE_ITEMS_CAN_NOT_EMPTY);
        }

        @Test
        void 주문을_저장한다() {
            // given
            Menu 맛있는_메뉴 = 맛있는_메뉴();
            OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(맛있는_메뉴.id(), 0);
            CreateOrderCommand command = new CreateOrderCommand(orderTable.id(), List.of(orderLineItemCommand));

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
            // given
            ChangeOrderStatusCommand command = new ChangeOrderStatusCommand(1L, null);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    orderService.changeOrderStatus(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_NOT_FOUND);
        }

        @Test
        void 완료된_주문의_상태를_변경하면_예외가_발생한다() {
            // given
            Order order = 완료된_주문();
            ChangeOrderStatusCommand command = new ChangeOrderStatusCommand(order.id(), OrderStatus.COOKING);

            // when
            BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                    orderService.changeOrderStatus(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS);
        }

        @Test
        void 주문의_상태를_변경한다() {
            // given
            Order order = 맛있는_메뉴_주문();
            ChangeOrderStatusCommand command = new ChangeOrderStatusCommand(order.id(), OrderStatus.MEAL);

            // when
            ChangeOrderStatusResponse result = orderService.changeOrderStatus(command);

            // then
            assertThat(result.orderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }
    }
}
