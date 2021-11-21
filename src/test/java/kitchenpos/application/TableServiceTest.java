package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.exception.CannotChangeOrderTableEmpty;
import kitchenpos.exception.CannotChangeOrderTableGuest;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.ui.dto.request.order.OrderLineItemRequestDto;
import kitchenpos.ui.dto.request.order.OrderRequestDto;
import kitchenpos.ui.dto.request.order.OrderStatusRequestDto;
import kitchenpos.ui.dto.request.table.OrderTableEmptyRequestDto;
import kitchenpos.ui.dto.request.table.OrderTableGuestRequestDto;
import kitchenpos.ui.dto.request.table.OrderTableRequestDto;
import kitchenpos.ui.dto.request.table.TableGroupRequestDto;
import kitchenpos.ui.dto.response.order.OrderResponseDto;
import kitchenpos.ui.dto.response.table.OrderTableResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class TableServiceTest {

    public static final Long INVALID_ID = 100L;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @DisplayName("[성공] 테이블 생성")
    @Test
    void create_Success() {
        // given
        OrderTableRequestDto orderTable = newOrderTable();

        // when
        OrderTableResponseDto createdOrderTable = tableService.create(orderTable);

        // then
        assertThat(createdOrderTable.getId()).isNotNull();
        assertThat(createdOrderTable)
            .extracting("tableGroupId", "numberOfGuests", "empty")
            .containsExactly(null, 0, true);
    }

    @DisplayName("[성공] 테이블 전체 조회")
    @Test
    void list_Success() {
        // given
        int previousSize = tableService.list().size();
        tableService.create(newOrderTable());

        // when
        List<OrderTableResponseDto> result = tableService.list();

        // then
        assertThat(result).hasSize(previousSize + 1);
    }

    @DisplayName("테이블 비어있는 여부 변경 테스트")
    @Nested
    class OrderTableChangeEmpty {

        @DisplayName("[성공] 비어있지 않은 테이블로 변경")
        @Test
        void changeEmpty_toNotEmpty_Success() {
            // given
            OrderTableResponseDto orderTable = tableService.create(newOrderTable());

            // when
            OrderTableResponseDto result = tableService.changeEmpty(
                orderTable.getId(), new OrderTableEmptyRequestDto(false)
            );

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result)
                .extracting("tableGroupId", "numberOfGuests", "empty")
                .containsExactly(null, 0, false);
        }

        @DisplayName("[성공] 비어있는 테이블로 변경")
        @Test
        void changeEmpty_Empty_Success() {
            // given
            OrderTableResponseDto orderTable = tableService.create(notEmptyOrderTable());

            // when
            OrderTableResponseDto result = tableService.changeEmpty(
                orderTable.getId(), new OrderTableEmptyRequestDto(true)
            );

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result)
                .extracting("tableGroupId", "numberOfGuests", "empty")
                .containsExactly(null, 0, true);
        }

        @DisplayName("[실패] 없는 테이블이면 예외 발생")
        @Test
        void changeEmpty_invalidTableId_ExceptionThrown() {
            // given
            // when
            // then
            assertThatThrownBy(
                () -> tableService.changeEmpty(INVALID_ID, new OrderTableEmptyRequestDto(false)))
                .isInstanceOf(InvalidOrderTableException.class);
        }

        @DisplayName("[실패] 그룹 테이블에 속한 테이블이면 예외 발생")
        @Test
        void changeEmpty_NotNullGroupId_ExceptionThrown() {
            // given
            Long orderTableInGroupId = saveAndReturnOrderTableId(newOrderTable());
            tableGroupService.create(
                new TableGroupRequestDto(
                    Arrays.asList(orderTableInGroupId, saveAndReturnOrderTableId(newOrderTable())))
            );

            // when
            // then
            assertThatThrownBy(() ->
                tableService.changeEmpty(orderTableInGroupId, new OrderTableEmptyRequestDto(false))
            ).isInstanceOf(CannotChangeOrderTableEmpty.class);
        }

        @DisplayName("[실패] 요리중/먹는중 테이블이면 예외 발생")
        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
        void changeEmpty_invalidTableId_ExceptionThrown(OrderStatus orderStatus) {
            // given
            Long orderTableId = saveAndReturnOrderTableId(notEmptyOrderTable());
            saveOrderWithStatus(orderTableId, orderStatus);

            // when
            // then
            assertThatThrownBy(() ->
                tableService.changeEmpty(orderTableId, new OrderTableEmptyRequestDto(true))
            ).isInstanceOf(CannotChangeOrderTableEmpty.class);
        }

        private void saveOrderWithStatus(Long tableId, OrderStatus status) {
            OrderResponseDto order = orderService.create(new OrderRequestDto(
                tableId,
                Collections.singletonList(new OrderLineItemRequestDto(1L, 1L))
            ));

            orderService.changeOrderStatus(order.getId(), new OrderStatusRequestDto(status.name()));
        }
    }

    @DisplayName("테이블 손님 수 변경 테스트")
    @Nested
    class OrderTableChangeNumberOfGuests {

        @DisplayName("[성공] 테이블의 손님 수를 양수로 변경")
        @Test
        void changeNumberOfGuests_PositiveNum_Success() {
            // given
            OrderTableResponseDto notEmptyOrderTable = tableService.changeEmpty(
                tableService.create(newOrderTable()).getId(), new OrderTableEmptyRequestDto(false)
            );

            // when
            OrderTableResponseDto result = tableService.changeNumberOfGuests(
                notEmptyOrderTable.getId(), new OrderTableGuestRequestDto(2)
            );

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getNumberOfGuests()).isEqualTo(2);
        }

        @DisplayName("[성공] 테이블의 손님 수를 0로 변경")
        @Test
        void changeNumberOfGuests_ZeroNum_Success() {
            // given
            OrderTableResponseDto notEmptyOrderTable = tableService.changeEmpty(
                tableService.create(newOrderTable()).getId(), new OrderTableEmptyRequestDto(false)
            );

            // when
            OrderTableResponseDto result = tableService.changeNumberOfGuests(
                notEmptyOrderTable.getId(), new OrderTableGuestRequestDto(0)
            );

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getNumberOfGuests()).isZero();
        }

        @DisplayName("[실패] 테이블의 손님 수를 음수로 변경 시 예외 발생")
        @Test
        void changeNumberOfGuests_NegativeNum_ExceptionThrown() {
            // given
            OrderTableResponseDto notEmptyOrderTable = tableService.changeEmpty(
                tableService.create(newOrderTable()).getId(), new OrderTableEmptyRequestDto(false)
            );

            // when
            // then
            assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(notEmptyOrderTable.getId(),
                    new OrderTableGuestRequestDto(-10))
            ).isInstanceOf(CannotChangeOrderTableGuest.class);
        }

        @DisplayName("[실패] 존재하지 않는 주문 테이블 이면 예외 발생")
        @Test
        void changeNumberOfGuests_InvalidOrderTable_ExceptionThrown() {
            // given
            // when
            // then
            assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(INVALID_ID, new OrderTableGuestRequestDto(2))
            ).isInstanceOf(InvalidOrderTableException.class);
        }

        @DisplayName("[실패] 비어있는 주문 테이블 이면 예외 발생")
        @Test
        void changeNumberOfGuests_EmptyOrderTable_ExceptionThrown() {
            // given
            OrderTableResponseDto emptyOrderTable = tableService.changeEmpty(
                tableService.create(newOrderTable()).getId(), new OrderTableEmptyRequestDto(true)
            );

            // when
            // then
            assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(emptyOrderTable.getId(), new OrderTableGuestRequestDto(2))
            ).isInstanceOf(CannotChangeOrderTableGuest.class);
        }
    }

    private Long saveAndReturnOrderTableId(OrderTableRequestDto orderTableRequestDto) {
        OrderTableResponseDto orderTable =
            tableService.create(orderTableRequestDto);

        return orderTable.getId();
    }

    private OrderTableRequestDto notEmptyOrderTable() {
        return new OrderTableRequestDto(0, false);
    }

    private OrderTableRequestDto newOrderTable() {
        return new OrderTableRequestDto(0, true);
    }
}
