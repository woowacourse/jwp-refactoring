package kitchenpos.application.order;

import static kitchenpos.exception.menu.MenuExceptionType.MENU_NOT_FOUND;
import static kitchenpos.exception.table.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.order.exception.OrderExceptionType.CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS;
import static kitchenpos.order.exception.OrderExceptionType.ORDER_LINE_ITEMS_CAN_NOT_EMPTY;
import static kitchenpos.order.exception.OrderExceptionType.ORDER_NOT_FOUND;
import static kitchenpos.order.exception.OrderExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.application.IntegrationTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.order.application.dto.ChangeOrderStatusCommand;
import kitchenpos.order.application.dto.ChangeOrderStatusResponse;
import kitchenpos.order.application.dto.CreateOrderCommand;
import kitchenpos.order.application.dto.CreateOrderResponse;
import kitchenpos.order.application.dto.OrderLineItemCommand;
import kitchenpos.order.application.dto.SearchOrderResponse;
import kitchenpos.order.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationTest {

    private OrderTable 안비어있는_테이블;

    @BeforeEach
    void setUp() {
        안비어있는_테이블 = 주문테이블저장(주문테이블(0, false));
    }

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

    @Test
    void 존재하지_않는_메뉴를_주문하면_예외가_발생한다() {
        // given
        Long noExistMenuId = 1L;
        OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(noExistMenuId, 0);
        CreateOrderCommand command = new CreateOrderCommand(안비어있는_테이블.id(), List.of(orderLineItemCommand));

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
        OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, true));
        Menu 메뉴 = 맛있는_메뉴_저장();
        OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(메뉴.id(), 0);
        CreateOrderCommand command = new CreateOrderCommand(주문테이블.id(), List.of(orderLineItemCommand));

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
        CreateOrderCommand command = new CreateOrderCommand(안비어있는_테이블.id(), List.of());

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
        Menu 메뉴 = 맛있는_메뉴_저장();
        OrderLineItemCommand orderLineItemCommand = new OrderLineItemCommand(메뉴.id(), 0);
        CreateOrderCommand command = new CreateOrderCommand(안비어있는_테이블.id(), List.of(orderLineItemCommand));

        // when
        CreateOrderResponse result = orderService.create(command);

        // then
        assertThat(result.id()).isPositive();
    }

    @Test
    void 주문들을_조회한다() {
        // given
        Menu 메뉴 = 맛있는_메뉴_저장();
        Order order1 = 주문저장(주문(안비어있는_테이블, COOKING, 주문항목(메뉴, 0)));
        Order order2 = 주문저장(주문(안비어있는_테이블, COOKING, 주문항목(메뉴, 0)));

        // when
        List<SearchOrderResponse> result = orderService.list();

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
        Long noExistOrderId = -1L;
        ChangeOrderStatusCommand command = new ChangeOrderStatusCommand(noExistOrderId, null);

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
        Menu 메뉴 = 맛있는_메뉴_저장();
        Order 완료된주문 = 주문저장(주문(안비어있는_테이블, COMPLETION, 주문항목(메뉴, 0)));
        ChangeOrderStatusCommand command = new ChangeOrderStatusCommand(완료된주문.id(), COOKING);

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
        Menu 메뉴 = 맛있는_메뉴_저장();
        Order order = 주문저장(주문(안비어있는_테이블, COOKING, 주문항목(메뉴, 0)));
        ChangeOrderStatusCommand command = new ChangeOrderStatusCommand(order.id(), MEAL);

        // when
        ChangeOrderStatusResponse result = orderService.changeOrderStatus(command);

        // then
        assertThat(result.orderStatus()).isEqualTo(MEAL.name());
    }
}
