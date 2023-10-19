package kitchenpos.application;

import static java.util.stream.Collectors.toUnmodifiableList;
import static kitchenpos.domain.exception.OrderExceptionType.ORDER_IS_NOT_COMPLETION;
import static kitchenpos.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED;
import static kitchenpos.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_PRESENT_ALL;
import static kitchenpos.domain.exception.TableGroupExceptionType.ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY;
import static kitchenpos.fixture.TableFixture.비어있는_전쳬_주문_테이블_DTO;
import static kitchenpos.fixture.TableFixture.비어있는_주문_테이블;
import static kitchenpos.fixture.TableFixture.비어있지_않는_전쳬_주문_테이블_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderException;
import kitchenpos.domain.exception.TableGroupException;
import kitchenpos.domain.exception.TableGroupExceptionType;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("Table Group을 추가한다.")
    class Create {

        @Test
        @DisplayName("테이블 그룹을 정상적으로 생성한다.")
        void success() {
            //given
            final List<OrderTableDto> orderTableDtos = 비어있는_전쳬_주문_테이블_DTO().stream()
                .map(tableService::create)
                .collect(Collectors.toList());

            final TableGroupDto tableGroupDto = new TableGroupDto(
                null,
                LocalDateTime.now(),
                orderTableDtos
            );

            //when
            final TableGroupDto savedTableGroup = tableGroupService.create(tableGroupDto);

            //then
            assertAll(
                () -> assertThat(savedTableGroup)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "orderTables.id", "orderTables.empty",
                        "orderTables.tableGroupId")
                    .isEqualTo(tableGroupDto),
                () -> assertThat(savedTableGroup.getOrderTables())
                    .extracting(OrderTableDto::getEmpty)
                    .doesNotContain(true)
            );
        }

        @Test
        @DisplayName("ordertable 이 비어있는 경우 예외처리한다.")
        void throwExceptionOrderTablesAreEmpty() {
            final TableGroupDto tableGroupDto = new TableGroupDto(null, LocalDateTime.now(),
                Collections.emptyList());

            assertThatThrownBy(() -> tableGroupService.create(tableGroupDto))
                .isInstanceOf(TableGroupException.class)
                .hasMessage(ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY.getMessage());
        }

        @Test
        @DisplayName("저장되지 않은 tableGroup로 요청하는 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotFound() {
            final TableGroupDto tableGroupDto
                = new TableGroupDto(null, LocalDateTime.now(), 비어있지_않는_전쳬_주문_테이블_DTO());

            assertThatThrownBy(() -> tableGroupService.create(tableGroupDto))
                .isInstanceOf(TableGroupException.class)
                .hasMessage(ORDER_TABLE_IS_NOT_PRESENT_ALL.getMessage());
        }

        @Test
        @DisplayName("tableGroup안에 orderTable이 비어있지 않은 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotEmpty() {
            final List<OrderTableDto> orderTableDtos = 비어있지_않는_전쳬_주문_테이블_DTO().stream()
                .map(tableService::create)
                .collect(toUnmodifiableList());

            final TableGroupDto tableGroupDto
                = new TableGroupDto(null, LocalDateTime.now(), orderTableDtos);

            assertThatThrownBy(() -> tableGroupService.create(tableGroupDto))
                .isInstanceOf(TableGroupException.class)
                .hasMessage(ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED.getMessage());
        }
    }

    @Nested
    @DisplayName("TableGroup을 해제했다.")
    class UnGroup {

        @Test
        @DisplayName("정상적으로 tableGroup을 해제한다.")
        void success() {
            //given
            final TableGroupDto savedTableGroupDto = saveTableGroupSuccessfully(
                비어있는_전쳬_주문_테이블_DTO());

            //when
            tableGroupService.ungroup(savedTableGroupDto.getId());

            //then
            final List<Long> savedOrderTableIds = savedTableGroupDto.getOrderTables()
                .stream()
                .map(OrderTableDto::getId)
                .collect(Collectors.toList());
            final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(
                savedOrderTableIds);

            assertThat(savedOrderTables)
                .extracting(OrderTable::getTableGroup, OrderTable::isEmpty)
                .containsExactly(
                    tuple(null, false), tuple(null, false), tuple(null, false), tuple(null, false),
                    tuple(null, false), tuple(null, false), tuple(null, false), tuple(null, false)
                );
        }

        @Test
        @DisplayName("해제하려는 TableGroup에 속한 Table의 주문 상태가 완료상태가 아닌경우 예외처리")
        void throwExceptionIfOrderIsNotCompletion() {
            //given
            final List<OrderTableDto> orderTableDtos = createOrderTableContainNoCompletion();
            final TableGroupDto tableGroupDto =
                new TableGroupDto(null, LocalDateTime.now(), orderTableDtos);

            final TableGroupDto savedTableGroupDto = tableGroupService.create(tableGroupDto);

            //when
            final Long tableGroupId = savedTableGroupDto.getId();
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(OrderException.class)
                .hasMessage(ORDER_IS_NOT_COMPLETION.getMessage());
        }

        private List<OrderTableDto> createOrderTableContainNoCompletion() {
            final OrderTable savedOrderTable = orderTableRepository.save(비어있는_주문_테이블());
            final OrderTable savedOrderTable2 = orderTableRepository.save(비어있는_주문_테이블());
            saveOrderMeal(savedOrderTable);

            return Stream.of(savedOrderTable, savedOrderTable2)
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
        }

        private void saveOrderMeal(final OrderTable savedOrderTable) {
            final Order order = new Order(
                savedOrderTable.getId(),
                OrderStatus.MEAL,
                LocalDateTime.now(),
                Map.of()
            );
            orderRepository.save(order);
        }
    }

    private TableGroupDto saveTableGroupSuccessfully(
        final List<OrderTableDto> orderTableDtos
    ) {
        final List<OrderTableDto> savedOrderTableDtos = orderTableDtos.stream()
            .map(tableService::create)
            .collect(Collectors.toList());
        final TableGroupDto tableGroupDto =
            new TableGroupDto(null, LocalDateTime.now(), savedOrderTableDtos);
        return tableGroupService.create(tableGroupDto);
    }
}
