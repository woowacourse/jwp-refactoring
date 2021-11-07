package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ServiceTest
class TableGroupServiceTest {

    @Mock
    private OrderDao mockOrderDao;

    @Mock
    private OrderTableDao mockOrderTableDao;

    @Mock
    private TableGroupDao mockTableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 생성")
    @Nested
    class CreateTableGroup {

        @BeforeEach
        void setUp() {
            when(mockOrderTableDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
        }

        @DisplayName("단체 지정을 생성한다.")
        @Test
        void create() {
            TableGroupRequest tableGroupRequest = createTableGroupRequest(
                    createOrderTable(1L, null, true),
                    createOrderTable(2L, null, true)
            );
            when(mockTableGroupDao.save(any())).thenReturn(tableGroupRequest.toEntity(1L));
            when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroupRequest.getOrderTables());

            TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);
            assertAll(
                    () -> assertThat(savedTableGroup).isNotNull(),
                    () -> assertThat(savedTableGroup.getId()).isNotNull(),
                    () -> assertThat(savedTableGroup.getOrderTables()).usingRecursiveComparison().isEqualTo(tableGroupRequest.getOrderTables())
            );
        }

        @DisplayName("단체 지정 대상 테이블은 이미 지정된 단체가 없어야한다.")
        @Test
        void createWithInvalidOrderTable1() {
            TableGroupRequest tableGroupRequest = createTableGroupRequest(
                    createOrderTable(1L, 1L, true),
                    createOrderTable(2L, 2L, true)
            );
            when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroupRequest.getOrderTables());
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블의 개수는 2개 이상이다.")
        @Test
        void createWithInvalidOrderTable2() {
            TableGroupRequest tableGroupRequest = createTableGroupRequest(createOrderTable(1L, null, true));
            when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroupRequest.getOrderTables());
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블은 비어있어야한다.")
        @Test
        void createWithInvalidOrderTable3() {
            TableGroupRequest tableGroupRequest =  createTableGroupRequest(
                    createOrderTable(false),
                    createOrderTable(false)
            );
            when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroupRequest.getOrderTables());
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정 해제")
    @Nested
    class UngroupTableGroup {

        @Captor
        private ArgumentCaptor<OrderTable> argument;
        private TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            tableGroup = createTableGroup(
                    createOrderTable(1L, null, true),
                    createOrderTable(2L, null, true)
            );
            when(mockOrderTableDao.findAllByTableGroupId(any())).thenReturn(tableGroup.getOrderTables());
        }

        @DisplayName("단체 지정을 해제한다.")
        @Test
        void ungroup() {
            when(mockOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                    any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            ).thenReturn(false);
            tableGroupService.ungroup(tableGroup.getId());

            verify(mockOrderTableDao, times(tableGroup.getOrderTables().size())).save(argument.capture());
            argument.getAllValues().forEach(orderTable -> {
                assertThat(orderTable.getTableGroupId()).isNull();
                assertThat(orderTable.isEmpty()).isFalse();
            });
        }

        @DisplayName("COOKING, MEAL 상태의 테이블 그룹은 해제할 수 없다.")
        @Test
        void ungroupWithInvalidStatusOrderTables() {
            when(mockOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                    any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            ).thenReturn(true);
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
