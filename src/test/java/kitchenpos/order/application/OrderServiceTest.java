package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_ALREADY_COMPLETION;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_LINE_ITEM_DTOS_EMPTY;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_LINE_ITEM_IS_NOT_PRESENT_ALL;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_TABLE_IS_EMPTY;
import static kitchenpos.support.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.support.fixture.TableFixture.비어있는_주문_테이블_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.support.ServiceIntegrationTest;
import kitchenpos.table.application.dto.OrderTableDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("order를 생성한다.")
    class Create {

        @Test
        @DisplayName("order를 성공적으로 생성한다.")
        void success() {
            //given
            final MenuDto menuDto = createMenu();
            final OrderLineItemDto orderLineItemDto = createOrderLineItem(menuDto.getId(), 1L);
            final OrderTableDto savedOrderTable = createNotEmptyOrderTable();

            final OrderDto orderDto = new OrderDto(
                null, savedOrderTable.getId(), COOKING.name(), LocalDateTime.now(),
                List.of(orderLineItemDto)
            );

            //when
            final OrderDto savedOrderDto = orderService.create(orderDto);

            //then
            assertThat(savedOrderDto)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems.seq", "orderLineItems.orderId")
                .isEqualTo(orderDto);
        }

        @Test
        @DisplayName("orderLineItem에 있는 menu가 존재하지 않는 경우 예외처리")
        void throwExceptionOrderLineItemsIsEmpty() {
            //given
            final long invalidatedId = Long.MIN_VALUE;
            final OrderLineItemDto orderLineItemDto = createOrderLineItem(invalidatedId, 1L);
            final OrderTableDto savedOrderTable = createNotEmptyOrderTable();

            final OrderDto orderDto = new OrderDto(
                null, savedOrderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItemDto)
            );

            //when
            assertThatThrownBy(() -> orderService.create(orderDto))
                .isInstanceOf(OrderException.class)
                .hasMessage(ORDER_LINE_ITEM_IS_NOT_PRESENT_ALL.getMessage());
        }

        @Test
        @DisplayName("orderTable이 비어있는 경우 예외처리")
        void throwExceptionOrderTableIsEmpty() {
            //given
            final MenuDto menuDto = createMenu();
            final OrderLineItemDto orderLineItemDto = createOrderLineItem(menuDto.getId(), 1L);
            final OrderTableDto savedOrderTable = tableService.create(비어있는_주문_테이블_DTO());

            final OrderDto orderDto = new OrderDto(
                null, savedOrderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItemDto)
            );

            //when
            assertThatThrownBy(() -> orderService.create(orderDto))
                .isInstanceOf(OrderException.class)
                .hasMessage(ORDER_TABLE_IS_EMPTY.getMessage());
        }

        @Test
        @DisplayName("orderLineItemDto가 비어있으면 예외처리")
        void throwExceptionOrderLineItemDtoIsEmpty() {
            //given
            final OrderTableDto savedOrderTable = tableService.create(비어있는_주문_테이블_DTO());

            final OrderDto orderDto = new OrderDto(
                null, savedOrderTable.getId(), null, LocalDateTime.now(), Collections.emptyList()
            );

            //when
            assertThatThrownBy(() -> orderService.create(orderDto))
                .isInstanceOf(OrderException.class)
                .hasMessage(ORDER_LINE_ITEM_DTOS_EMPTY.getMessage());
        }
    }

    @Test
    @DisplayName("order를 list로 조회한다.")
    void list() {
        //given
        final OrderDto orderDto = createOrderSuccessfully();

        //when
        final List<OrderDto> foundOrders = orderService.list();

        //then
        assertThat(foundOrders)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
            .containsExactly(orderDto);
    }

    @Nested
    @DisplayName("order의 상태를 바꾼다.")
    class ChangeOrderStatus {

        @Test
        @DisplayName("정상적으로 바꾼다.")
        void success() {
            //given
            final OrderDto cookingOrder = createOrderSuccessfully();
            final OrderDto parameter = new OrderDto(
                cookingOrder.getId(),
                cookingOrder.getOrderTableId(),
                OrderStatus.MEAL.name(),
                cookingOrder.getOrderedTime(),
                cookingOrder.getOrderLineItems()
            );

            //when
            orderService.changeOrderStatus(cookingOrder.getId(), parameter);

            //then
            final Order foundOrder = orderRepository.findById(cookingOrder.getId())
                .orElseThrow(RuntimeException::new);
            assertThat(foundOrder.getOrderStatus())
                .isEqualTo(OrderStatus.MEAL);
        }

        @Test
        @DisplayName("변경하려는 order의 상태가 completion인 경우 예외처리")
        void throwExceptionOrderStatusIsCompletion() {
            //given
            final OrderDto cookingOrderDto = createOrderSuccessfully();
            final OrderDto parameter = new OrderDto(
                cookingOrderDto.getId(),
                cookingOrderDto.getOrderTableId(),
                OrderStatus.COMPLETION.name(),
                cookingOrderDto.getOrderedTime(),
                cookingOrderDto.getOrderLineItems()
            );
            orderService.changeOrderStatus(cookingOrderDto.getId(), parameter);

            //when
            assertThatThrownBy(
                () -> orderService.changeOrderStatus(cookingOrderDto.getId(), parameter)
            ).isInstanceOf(OrderException.class)
                .hasMessage(ORDER_IS_ALREADY_COMPLETION.getMessage());
        }
    }
}
