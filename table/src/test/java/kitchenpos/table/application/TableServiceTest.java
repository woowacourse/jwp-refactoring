package kitchenpos.table.application;

import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_NOT_COMPLETION;
import static kitchenpos.support.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.support.fixture.TableFixture.비어있는_전쳬_주문_테이블_DTO;
import static kitchenpos.support.fixture.TableFixture.비어있는_주문_테이블_DTO;
import static kitchenpos.support.fixture.TableFixture.비어있지_않는_주문_테이블_DTO;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.NUMBER_OF_GUEST_LOWER_THAN_ZERO;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.support.ServiceIntegrationTest;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.exception.OrderTableException;
import kitchenpos.table_group.application.dto.OrderTableDtoInTableGroup;
import kitchenpos.table_group.application.dto.TableGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceIntegrationTest {

    @Test
    @DisplayName("table을 생성한다.")
    void create() {
        final OrderTableDto orderTableDto = TableFixture.비어있는_주문_테이블_DTO();

        final OrderTableDto savedOrderTableDto = tableService.create(orderTableDto);

        assertThat(savedOrderTableDto)
            .usingRecursiveComparison()
            .ignoringFields("id", "tableGroupId")
            .isEqualTo(orderTableDto);
    }

    @Test
    @DisplayName("table 목록을 조회한다.")
    void list() {
        final List<OrderTableDto> orderTables = TableFixture.비어있는_전쳬_주문_테이블_DTO().stream()
            .map(tableService::create)
            .collect(Collectors.toList());

        final List<OrderTableDto> foundedOrderTables = tableService.list();

        Assertions.assertThat(foundedOrderTables)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
            .isEqualTo(orderTables);
    }

    @Nested
    @DisplayName("table의 상태를 empty로 바꾼다.")
    class ChangeEmpty {

        @Test
        @DisplayName("성공적으로 바꾼다.")
        void success() {
            final OrderTableDto orderTableDto = tableService.create(TableFixture.비어있는_주문_테이블_DTO());
            final OrderTableDto parameter = TableFixture.비어있지_않는_주문_테이블_DTO();

            final OrderTableDto changedOrderTable = tableService.changeEmpty
                (orderTableDto.getId(), parameter);

            assertThat(changedOrderTable.getEmpty())
                .isEqualTo(parameter.getEmpty());
        }

        @Test
        @DisplayName("tableGroupId가 null이 아닌 경우 예외처리한다.")
        void throwExceptionTableGroupIdIsNonNull() {
            final OrderTableDto orderTableDto = TableFixture.비어있는_주문_테이블_DTO();
            final OrderTableDto savedOrderTableDto = tableService.create(orderTableDto);
            saveTableGroup(savedOrderTableDto);

            final Long orderTableId = savedOrderTableDto.getId();
            Assertions.assertThatThrownBy(
                () -> tableService.changeEmpty(orderTableId, savedOrderTableDto)
            ).isInstanceOf(OrderTableException.class)
                .hasMessage(TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP.getMessage());
        }

        @Test
        @DisplayName("저장된 table에 속한 order가 cooking이나 meal 상태인 경우 예외처리")
        void throwExceptionTableIsEmpty() {
            //given
            final OrderTableDto orderTableDto = TableFixture.비어있지_않는_주문_테이블_DTO();
            final OrderTableDto savedOrderTableDto = tableService.create(orderTableDto);
            createOrderSuccessfully(savedOrderTableDto);

            //when
            final Long orderTableId = savedOrderTableDto.getId();
            Assertions.assertThatThrownBy(
                () -> tableService.changeEmpty(orderTableId, savedOrderTableDto)
            ).isInstanceOf(OrderException.class)
                .hasMessage(ORDER_IS_NOT_COMPLETION.getMessage());
        }

        private void createOrderSuccessfully(final OrderTableDto orderTableDto) {
            final MenuDto menuDto = createMenu();
            final OrderLineItemDto orderLineItemDto = OrderFixture.createOrderLineItem(menuDto.getId(), 1L);

            final OrderDto orderDto = new OrderDto(
                null,
                orderTableDto.getId(),
                null,
                LocalDateTime.now(),
                List.of(orderLineItemDto)
            );
            orderService.create(orderDto);
        }
    }

    @Nested
    @DisplayName("게스트의 수를 변경할 수 있다.")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("정상적으로 변경한다.")
        void success() {
            //given
            final OrderTableDto orderTableDto = TableFixture.비어있지_않는_주문_테이블_DTO();
            final OrderTableDto savedOrderTableDto = tableService.create(orderTableDto);

            final OrderTableDto parameter = new OrderTableDto(savedOrderTableDto.getId(),
                savedOrderTableDto.getTableGroupId(), 100, savedOrderTableDto.getEmpty());

            //when
            final OrderTableDto changedOrderTableDto =
                tableService.changeNumberOfGuests(savedOrderTableDto.getId(), parameter);

            //then
            assertThat(changedOrderTableDto.getNumberOfGuests())
                .isEqualTo(parameter.getNumberOfGuests());
        }

        @Test
        @DisplayName("numberOfGuest가 0미만인 경우 예외처리")
        void throwExceptionNumberOfGuestLowerThan0() {
            //given
            final OrderTableDto orderTableDto = TableFixture.비어있지_않는_주문_테이블_DTO();
            final OrderTableDto savedOrderTableDto = tableService.create(orderTableDto);

            final OrderTableDto parameter = new OrderTableDto(savedOrderTableDto.getId(),
                savedOrderTableDto.getTableGroupId(), -1, savedOrderTableDto.getEmpty());

            //when
            final Long savedOrderTableId = savedOrderTableDto.getId();
            Assertions.assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(savedOrderTableId, parameter)
            ).isInstanceOf(OrderTableException.class)
                .hasMessage(NUMBER_OF_GUEST_LOWER_THAN_ZERO.getMessage());
        }

        @Test
        @DisplayName("orderTable이 비어있는 경우 예외처리")
        void throwExceptionOrderTableIsEmpty() {
            //given
            final OrderTableDto orderTableDto = TableFixture.비어있는_주문_테이블_DTO();
            final OrderTableDto savedOrderTableDto = tableService.create(orderTableDto);

            final OrderTableDto parameter = new OrderTableDto(savedOrderTableDto.getId(),
                savedOrderTableDto.getTableGroupId(), 10, savedOrderTableDto.getEmpty());

            //when
            final Long tableId = savedOrderTableDto.getId();
            Assertions.assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(tableId, parameter)
            ).isInstanceOf(OrderTableException.class)
                .hasMessage(TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY.getMessage());
        }
    }

    private void saveTableGroup(final OrderTableDto savedOrderTableDto) {
        final OrderTableDto orderTable = tableService.create(TableFixture.비어있는_주문_테이블_DTO());

        final List<OrderTableDtoInTableGroup> orderTables = List.of(
            ServiceIntegrationTest.map(orderTable), ServiceIntegrationTest.map(savedOrderTableDto));

        final TableGroupDto tableGroupDto = new TableGroupDto(
            null,
            LocalDateTime.now(),
            orderTables
        );

        tableGroupService.create(tableGroupDto);
    }
}
