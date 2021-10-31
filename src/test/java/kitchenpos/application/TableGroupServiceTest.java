package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.factory.OrderTableFactory;
import kitchenpos.factory.TableGroupFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Captor
    ArgumentCaptor<OrderTable> orderTableArgumentCaptor;

    @Nested
    class CreateTest {

        OrderTable orderTable1;
        OrderTable orderTable2;
        List<OrderTable> orderTables;

        OrderTable savedOrderTable1;
        OrderTable savedOrderTable2;
        List<OrderTable> savedOrderTables;

        List<Long> orderTableIds;

        TableGroup tableGroup;

        TableGroup savedTableGroup;

        @BeforeEach
        void setUp() {
            orderTable1 = OrderTableFactory.builder()
                .id(1L)
                .tableGroupId(null)
                .numberOfGuests(0)
                .empty(true)
                .build();

            orderTable2 = OrderTableFactory.builder()
                .id(2L)
                .tableGroupId(null)
                .numberOfGuests(0)
                .empty(true)
                .build();

            orderTables = Arrays.asList(
                orderTable1,
                orderTable2
            );

            orderTableIds = Arrays.asList(
                orderTable1.getId(),
                orderTable2.getId()
            );

            savedOrderTable1 = OrderTableFactory.copy(orderTable1).build();

            savedOrderTable2 = OrderTableFactory.copy(orderTable2).build();

            savedOrderTables = Arrays.asList(
                savedOrderTable1,
                savedOrderTable2
            );

            tableGroup = TableGroupFactory.builder()
                .orderTables(orderTables)
                .build();

            savedTableGroup = TableGroupFactory.copy(tableGroup)
                .id(1L)
                .orderTables(savedOrderTables)
                .build();
        }

        @DisplayName("TableGroup 을 생성한다")
        @Test
        void create() {
            // given
            given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);
            given(tableGroupDao.save(tableGroup)).willReturn(savedTableGroup);

            // when
            TableGroup result = tableGroupService.create(this.tableGroup);

            // then
            verify(orderTableDao, times(2)).save(orderTableArgumentCaptor.capture());
            List<OrderTable> orderTableArguments = orderTableArgumentCaptor.getAllValues();
            assertThat(orderTableArguments)
                .containsExactly(
                    savedOrderTable1,
                    savedOrderTable2
                );
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedTableGroup);
        }

        @DisplayName("TableGroup 생성 실패한다 - 요청한 orderTables 가 비어있는 경우")
        @Test
        void createFail_whenOrderTablesAreEmpty() {
            // given
            tableGroup = TableGroupFactory.copy(tableGroup)
                .orderTables(Collections.emptyList())
                .build();

            // when // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup 생성 실패한다 - 요청한 orderTable 의 수가 2 개 미만인 경우")
        @Test
        void createFail_whenOrderTablesAreLessThanTwo() {
            // given
            tableGroup = TableGroupFactory.copy(tableGroup)
                .orderTables(Collections.singletonList(orderTable1))
                .build();

            // when // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup 생성 실패한다 - 요청한 orderTable 중 존재하지 않는 것이 있는 경우")
        @Test
        void createFail_whenOrderTableDoesNotExist() {
            // given
            given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(
                Collections.singletonList(savedOrderTable1));

            // when // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup 생성 실패한다 - 요청한 orderTable 이 비어있지 않는 경우")
        @Test
        void createFail_whenOrderTableIsNotEmpty() {
            // given
            savedOrderTable1 = OrderTableFactory.copy(savedOrderTable1)
                .empty(false)
                .build();
            savedOrderTables = Arrays.asList(
                savedOrderTable1,
                savedOrderTable2
            );
            given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

            // when // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("TableGroup 생성 실패한다 - 요청한 orderTable 이 이미 tableGroup 을 가지고 있는 경우")
        @Test
        void createFail_whenOrderTableAlreadyHasTableGroupId() {
            // given
            savedOrderTable1 = OrderTableFactory.copy(savedOrderTable1)
                .tableGroupId(2L)
                .build();
            savedOrderTables = Arrays.asList(
                savedOrderTable1,
                savedOrderTable2
            );
            given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

            // when // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class UngroupTest {

        OrderTable orderTable1;
        OrderTable orderTable2;
        List<OrderTable> orderTables;

        List<Long> orderTableIds;

        TableGroup tableGroup;
        Long tableGroupId;

        List<String> notCompletionOrderStatuses;

        @BeforeEach
        void setUp() {
            tableGroup = TableGroupFactory.builder()
                .id(1L)
                .orderTables(orderTables)
                .build();

            tableGroupId = tableGroup.getId();

            orderTable1 = OrderTableFactory.builder()
                .id(1L)
                .tableGroupId(tableGroupId)
                .numberOfGuests(0)
                .empty(false)
                .build();

            orderTable2 = OrderTableFactory.builder()
                .id(2L)
                .tableGroupId(tableGroupId)
                .numberOfGuests(0)
                .empty(false)
                .build();

            orderTables = Arrays.asList(
                orderTable1,
                orderTable2
            );

            orderTableIds = Arrays.asList(
                orderTable1.getId(),
                orderTable2.getId()
            );

            notCompletionOrderStatuses = Arrays.asList(OrderStatus.COOKING.name(),
                OrderStatus.MEAL.name());
        }

        @Test
        void ungroup() {
            // given
            given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds,
                notCompletionOrderStatuses
            )).willReturn(false);

            // when
            tableGroupService.ungroup(tableGroupId);

            // then
            verify(orderTableDao, times(2)).save(orderTableArgumentCaptor.capture());
            List<OrderTable> orderTableArguments = orderTableArgumentCaptor.getAllValues();
            assertThat(orderTableArguments)
                .containsExactly(
                    orderTable1,
                    orderTable2
                );
        }

        @DisplayName("TableGroup 을 ungroup 하는 것을 실패한다 - 요청한 orderTable 의 order 상태가 COMPLETION 이 아닌 경우")
        @Test
        void ungroupFail_whenOrderTableStatusIsNotCompletion() {
            // given
            given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds,
                notCompletionOrderStatuses
            )).willReturn(true);

            // when // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
