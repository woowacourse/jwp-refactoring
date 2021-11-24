package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.org.apache.xpath.internal.operations.Or;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.CannotChangeOrderStatus;
import kitchenpos.exception.InvalidMenuException;
import kitchenpos.exception.InvalidOrderException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.ui.dto.request.order.OrderLineItemRequestDto;
import kitchenpos.ui.dto.request.order.OrderRequestDto;
import kitchenpos.ui.dto.request.order.OrderStatusRequestDto;
import kitchenpos.ui.dto.request.table.OrderTableEmptyRequestDto;
import kitchenpos.ui.dto.request.table.OrderTableRequestDto;
import kitchenpos.ui.dto.response.order.OrderLineItemResponseDto;
import kitchenpos.ui.dto.response.order.OrderResponseDto;
import kitchenpos.ui.dto.response.table.OrderTableResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("메뉴 그룹 서비스 통합 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class OrderServiceTest {

    public static final Long INVALID_ID = 100L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @DisplayName("주문 생성 테스트")
    @Nested
    class OrderCreate {

        @DisplayName("[성공] 새로운 주문 등록")
        @Test
        void create_Success() {
            // given
            OrderRequestDto order = newOrder();

            // when
            OrderResponseDto createdOrder = orderService.create(order);

            // then
            assertThat(createdOrder.getId()).isNotNull();
            assertThat(createdOrder).extracting("orderTableId", "orderStatus")
                .contains(order.getOrderTableId(), "COOKING");
            assertThat(createdOrder.getOrderLineItems().stream()
                .map(OrderLineItemResponseDto::getSeq)
                .filter(Objects::nonNull)
                .count())
                .isEqualTo(order.getOrderLineItems().size());
        }

        @DisplayName("[실패] 주문항목이 비어있을 경우 예외 발생")
        @Test
        void create_EmptyOrderLineItems_ExceptionThrown() {
            // given
            OrderRequestDto order = newOrderWithEmptyOrderLine();

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(InvalidOrderException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 주문항목의 메뉴가 존재하지 않을 경우 예외 발생")
        @Test
        void create_InvalidMenu_ExceptionThrown() {
            // given
            OrderRequestDto order = newOrderWithInvalidMenu();

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(InvalidMenuException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 주문항목의 주문 테이블이 존재하지 않을 경우 예외 발생")
        @Test
        void create_InvalidOrderTable_ExceptionThrown() {
            // given
            OrderRequestDto order = newOrderWithInvalidTable();

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(InvalidOrderTableException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 주문항목의 주문 테이블이 비어있을 경우 예외 발생")
        @Test
        void create_EmptyOrderTable_ExceptionThrown() {
            // given
            OrderRequestDto order = newOrderWithEmptyTable();

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(InvalidOrderException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class OrderChangeStatus {
        @DisplayName("[성공] 주문 상태 변경")
        @Test
        void changeOrderStatus_Success() {
            // given
            OrderResponseDto order = orderService.create(newOrder());
            OrderStatusRequestDto orderStatusRequestDto = new OrderStatusRequestDto(OrderStatus.MEAL.name());

            // when
            OrderResponseDto result = orderService.changeOrderStatus(order.getId(), orderStatusRequestDto);

            // then
            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @DisplayName("[실패] 주문 ID가 유효하지 않으면 예외 발생")
        @Test
        void changeOrderStatus_InvalidOrderId_ExceptionThrown() {
            // given
            OrderStatusRequestDto orderStatusRequestDto = new OrderStatusRequestDto(OrderStatus.MEAL.name());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(INVALID_ID, orderStatusRequestDto))
                .isInstanceOf(InvalidOrderException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 주문이 Completion 상태라면 예외 발생")
        @Test
        void changeOrderStatus_CompletionOrderStatus_ExceptionThrown() {
            // given
            OrderResponseDto order = orderService.create(newOrder());
            orderService.changeOrderStatus(
                order.getId(),
                new OrderStatusRequestDto(OrderStatus.COMPLETION.name())
            );
            OrderStatusRequestDto orderStatusRequestDto =
                new OrderStatusRequestDto(OrderStatus.MEAL.name());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderStatusRequestDto))
                .isInstanceOf(CannotChangeOrderStatus.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }
    }

    @DisplayName("[성공] 주문 항목 전체 조회")
    @Test
    void list_Success() {
        // given
        int previousSize = orderService.list().size();
        orderService.create(newOrder());

        // when
        List<OrderResponseDto> result = orderService.list();

        // then
        assertThat(result).hasSize(previousSize + 1);
    }

    private OrderRequestDto newOrder() {
        return new OrderRequestDto(
            saveAndReturnOrderTableId(false),
            Collections.singletonList(newOrderLineItem())
        );
    }

    private OrderRequestDto newOrderWithEmptyOrderLine() {
        return new OrderRequestDto(
            saveAndReturnOrderTableId(false),
            null
        );
    }

    private OrderRequestDto newOrderWithInvalidMenu() {
        return new OrderRequestDto(
            saveAndReturnOrderTableId(false),
            Collections.singletonList(new OrderLineItemRequestDto(INVALID_ID, 1L))
        );
    }

    private OrderRequestDto newOrderWithInvalidTable() {
        return new OrderRequestDto(
            INVALID_ID,
            Collections.singletonList(newOrderLineItem())
        );
    }

    private OrderRequestDto newOrderWithEmptyTable() {
        return new OrderRequestDto(
            saveAndReturnOrderTableId(true),
            Collections.singletonList(newOrderLineItem())
        );
    }

    private OrderLineItemRequestDto newOrderLineItem() {
        return new OrderLineItemRequestDto(1L, 1L);
    }

    private Long saveAndReturnOrderTableId(boolean empty) {
        OrderTableResponseDto orderTable =
            tableService.create(new OrderTableRequestDto(0, true));

        OrderTableEmptyRequestDto orderTableEmptyRequestDto = new OrderTableEmptyRequestDto(empty);
        return tableService.changeEmpty(orderTable.getId(), orderTableEmptyRequestDto).getId();
    }
}
