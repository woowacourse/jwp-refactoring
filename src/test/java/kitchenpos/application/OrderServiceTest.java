package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.request.CreateOrderDto;
import kitchenpos.application.dto.request.CreateOrderLineItemDto;
import kitchenpos.application.dto.request.CreateTableDto;
import kitchenpos.application.dto.response.OrderDto;
import kitchenpos.application.dto.request.UpdateOrderStatusDto;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 주문을_생성하고_반환한다() {
            Long orderTableId = saveOrderTable();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(1L, 1));
            OrderDto actual = orderService.create(new CreateOrderDto(orderTableId, orderLineItems));
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(actual.getOrderedTime()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        void 주문하는_메뉴_정보가_없는_경우_예외가_발생한다() {
            Long orderTableId = saveOrderTable();
            assertThatThrownBy(() -> orderService.create(new CreateOrderDto(orderTableId, List.of())))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴가_포함된_경우_예외가_발생한다() {
            Long orderTableId = saveOrderTable();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(9999999L, 1));
            assertThatThrownBy(() -> orderService.create(new CreateOrderDto(orderTableId, orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블에서_주문한_경우_예외가_발생한다() {
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(1L, 1));
            assertThatThrownBy(() -> orderService.create(new CreateOrderDto(99999999L, orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블에서_주문한_경우_예외가_발생한다() {
            Long orderTableId = tableService.create(new CreateTableDto(0, true)).getId();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(1L, 1));
            assertThatThrownBy(() -> orderService.create(new CreateOrderDto(orderTableId, orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void list_메서드는_주문_목록을_조회한다() {
        List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(1L, 1));
        orderService.create(new CreateOrderDto(saveOrderTable(), orderLineItems));
        orderService.create(new CreateOrderDto(saveOrderTable(), orderLineItems));

        List<OrderDto> orders = orderService.list();
        assertThat(orders).hasSizeGreaterThan(1);
    }

    @DisplayName("changeOrderStatus 메서드는")
    @Nested
    class ChangeOrderStatusTest {

        @Test
        void 주문의_상태를_수정하고_반환한다() {
            Long orderTableId = saveOrderTable();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(1L, 1));
            Long orderId = orderService.create(new CreateOrderDto(orderTableId, orderLineItems)).getId();

            UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(orderId, OrderStatus.MEAL);
            OrderDto actual = orderService.changeOrderStatus(updateOrderStatusDto);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
                    () -> assertThat(actual.getOrderedTime()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        void 존재하지_않는_주문인_경우_예외를_발생시킨다() {
            UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(99999L, OrderStatus.MEAL);
            assertThatThrownBy(() -> orderService.changeOrderStatus(updateOrderStatusDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void COMPLETION_상태인_주문의_상태롤_변경하려는_경우_예외를_발생시킨다() {
            Long orderTableId = saveOrderTable();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(1L, 1));
            Long orderId = orderService.create(new CreateOrderDto(orderTableId, orderLineItems)).getId();

            UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(orderId, OrderStatus.COMPLETION);
            orderService.changeOrderStatus(updateOrderStatusDto);
            assertThatThrownBy(() -> orderService.changeOrderStatus(updateOrderStatusDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Long saveOrderTable() {
        return tableService.create(new CreateTableDto(1, false)).getId();
    }
}
