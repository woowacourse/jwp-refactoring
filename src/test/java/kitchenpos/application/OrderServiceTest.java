package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.exception.OrderExceptionType.EMPTY_ORDER_LINE_ITEM_EXCEPTION;
import static kitchenpos.exception.OrderExceptionType.ORDER_STATUS_ALREADY_COMPLETION_EXCEPTION;
import static kitchenpos.exception.OrderExceptionType.ORDER_TABLE_EMPTY_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.annotation.IntegrationTest;
import kitchenpos.dao.jpa.JpaOrderRepository;
import kitchenpos.dao.jpa.JpaOrderTableRepository;
import kitchenpos.dao.jpa.JpaTableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.CreateOrderLineItemRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.OrderException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JpaOrderTableRepository orderTableRepository;
    @Autowired
    private JpaTableGroupRepository tableGroupRepository;
    @Autowired
    private JpaOrderRepository orderRepository;

    @Nested
    class 주문_저장 {

        @Test
        void 주문_항목이_없으면_예외가_발생한다() {
            // given
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, false));
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.id(), List.of());

            // when
            BaseExceptionType exceptionType = assertThrows(OrderException.class, () ->
                    orderService.create(orderCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(EMPTY_ORDER_LINE_ITEM_EXCEPTION);
        }

        @Test
        void 테이블이_주문할_수_없는_상태이면_예외를_발생한다() {
            // given
            List<CreateOrderLineItemRequest> createOrderLineItemRequests = List.of(
                    new CreateOrderLineItemRequest(1L, 1L),
                    new CreateOrderLineItemRequest(2L, 2L)
            );
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, createOrderLineItemRequests);

            // when
            BaseExceptionType exceptionType = assertThrows(OrderException.class, () ->
                    orderService.create(orderCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_TABLE_EMPTY_EXCEPTION);
        }
    }

    @Nested
    class 모든_주문을_조회한다 {

        @Test
        void 모든_주문을_성공적으로_조회한다() {
            // when
            List<Order> orders = orderService.list();

            // then
            assertThat(orders).isNotNull();
            assertThat(orders.size()).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    class 주문_상태를_변경한다 {

        @Test
        void 주문의_상태가_이미_계산완료면_예외가_발생한다() {
            // given
            tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 10, false));
            Order order = orderRepository.save(new Order(orderTable, COMPLETION, LocalDateTime.now()));

            // when
            BaseExceptionType exceptionType = assertThrows(OrderException.class, () ->
                    orderService.changeOrderStatus(order.id(), OrderStatus.COOKING)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_STATUS_ALREADY_COMPLETION_EXCEPTION);
        }
    }
}
