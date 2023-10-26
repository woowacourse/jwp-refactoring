package kitchenpos.table_group.application;

import static java.util.stream.Collectors.toUnmodifiableList;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_NOT_COMPLETION;
import static kitchenpos.support.fixture.TableFixture.비어있는_전쳬_주문_테이블_DTO;
import static kitchenpos.support.fixture.TableFixture.비어있는_주문_테이블;
import static kitchenpos.support.fixture.TableFixture.비어있지_않는_전쳬_주문_테이블_DTO;
import static kitchenpos.table_group.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_EMPTY;
import static kitchenpos.table_group.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_PRESENT_ALL;
import static kitchenpos.table_group.domain.exception.TableGroupExceptionType.ORDER_TABLE_SIZE_IS_LOWER_THAN_TWO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.support.ServiceIntegrationTest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table_group.application.dto.OrderTableDtoInTableGroup;
import kitchenpos.table_group.application.dto.TableGroupDto;
import kitchenpos.table_group.domain.exception.TableGroupException;
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
            final List<OrderTableDtoInTableGroup> orderTableDtos = 비어있는_전쳬_주문_테이블_DTO().stream()
                .map(tableService::create)
                .map(TableGroupServiceTest::map)
                .collect(Collectors.toList());

            final TableGroupDto tableGroupDto = new TableGroupDto(
                null,
                LocalDateTime.now(),
                orderTableDtos
            );

            //when
            final TableGroupDto savedTableGroup = tableGroupService.create(tableGroupDto);

            //then
            final Set<Long> tableGroupIds = tableService.list()
                .stream()
                .map(OrderTableDto::getTableGroupId)
                .collect(Collectors.toUnmodifiableSet());

            assertThat(tableGroupIds)
                .containsExactly(savedTableGroup.getId());
        }

        @Test
        @DisplayName("ordertable 이 비어있는 경우 예외처리한다.")
        void throwExceptionOrderTablesAreEmpty() {
            final TableGroupDto tableGroupDto = new TableGroupDto(null, LocalDateTime.now(),
                Collections.emptyList());

            assertThatThrownBy(() -> tableGroupService.create(tableGroupDto))
                .isInstanceOf(TableGroupException.class)
                .hasMessage(ORDER_TABLE_SIZE_IS_LOWER_THAN_TWO.getMessage());
        }

        @Test
        @DisplayName("저장되지 않은 tableGroup로 요청하는 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotFound() {
            final OrderTableDtoInTableGroup unsavedTable1
                = new OrderTableDtoInTableGroup(1L, null, 1, true);
            final OrderTableDtoInTableGroup unsavedTable2
                = new OrderTableDtoInTableGroup(2L, null, 1, true);

            final TableGroupDto tableGroupDto = new TableGroupDto(null, LocalDateTime.now(),
                List.of(unsavedTable1, unsavedTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupDto))
                .isInstanceOf(TableGroupException.class)
                .hasMessage(ORDER_TABLE_IS_NOT_PRESENT_ALL.getMessage());
        }

        @Test
        @DisplayName("tableGroup안에 orderTable이 비어있지 않은 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotEmpty() {
            final List<OrderTableDtoInTableGroup> orderTableDtos = 비어있지_않는_전쳬_주문_테이블_DTO().stream()
                .map(tableService::create)
                .map(ServiceIntegrationTest::map)
                .collect(toUnmodifiableList());

            final TableGroupDto tableGroupDto
                = new TableGroupDto(null, LocalDateTime.now(), orderTableDtos);

            assertThatThrownBy(() -> tableGroupService.create(tableGroupDto))
                .isInstanceOf(TableGroupException.class)
                .hasMessage(ORDER_TABLE_IS_NOT_EMPTY.getMessage());
        }
    }

    @Nested
    @DisplayName("TableGroup을 해제했다.")
    class UnGroup {

        @Test
        @DisplayName("정상적으로 tableGroup을 해제한다.")
        void success() {
            //given
            final TableGroupDto savedTableGruopDto = saveTableGroupSuccessfully(
                비어있는_전쳬_주문_테이블_DTO());

            //when
            tableGroupService.ungroup(savedTableGruopDto.getId());

            //then
            assertThat(tableService.list())
                .extracting(OrderTableDto::getTableGroupId, OrderTableDto::getEmpty)
                .containsExactly(
                    tuple(null, true), tuple(null, true), tuple(null, true), tuple(null, true),
                    tuple(null, true), tuple(null, true), tuple(null, true), tuple(null, true)
                );
        }

        @Test
        @DisplayName("해제하려는 TableGroup에 속한 Table의 주문 상태가 완료상태가 아닌경우 예외처리")
        void throwExceptionIfOrderIsNotCompletion() {
            //given
            final List<OrderTableDtoInTableGroup> orderTableDtos = createOrderTableContainNoCompletion();
            final TableGroupDto tableGroupDto =
                new TableGroupDto(null, LocalDateTime.now(), orderTableDtos);

            final TableGroupDto savedTableGruopDto = tableGroupService.create(tableGroupDto);

            //when
            final Long tableGroupId = savedTableGruopDto.getId();
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(OrderException.class)
                .hasMessage(ORDER_IS_NOT_COMPLETION.getMessage());
        }

        private List<OrderTableDtoInTableGroup> createOrderTableContainNoCompletion() {
            final OrderTable savedOrderTable = orderTableRepository.save(비어있는_주문_테이블());
            final OrderTable savedOrderTable2 = orderTableRepository.save(비어있는_주문_테이블());
            saveOrderMeal(savedOrderTable);

            return Stream.of(savedOrderTable, savedOrderTable2)
                .map(OrderTableDto::from)
                .map(ServiceIntegrationTest::map)
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
        final List<OrderTableDtoInTableGroup> savedOrderTableDtos = orderTableDtos.stream()
            .map(tableService::create)
            .map(ServiceIntegrationTest::map)
            .collect(Collectors.toList());
        final TableGroupDto tableGroupDto =
            new TableGroupDto(null, LocalDateTime.now(), savedOrderTableDtos);
        return tableGroupService.create(tableGroupDto);
    }
}
