package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = createTableGroup(
                createOrderTable(1L, null, true),
                createOrderTable(2L, null, true)
        );
        when(mockTableGroupDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
        when(mockOrderTableDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
    }

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroup.getOrderTables());
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        assertThat(savedTableGroup).isEqualTo(tableGroup);
    }

    @DisplayName("단체 지정 대상 테이블의 개수는 2개 이상이다.")
    @Test
    void createWithInvalidOrderTable1() {
        tableGroup.setOrderTables(Collections.singletonList(createOrderTable(1L, null, true)));
        when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroup.getOrderTables());
        assertThatThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정 대상 테이블은 기존 단체가 없어야한다.")
    @Test
    void createWithInvalidOrderTable2() {
        tableGroup.setOrderTables(Arrays.asList(
                createOrderTable(1L, 1L, true),
                createOrderTable(2L, 2L, true)
        ));
        when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroup.getOrderTables());
        assertThatThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정 대상 테이블은 비어있어야한다.")
    @Test
    void createWithInvalidOrderTable3() {
        tableGroup.setOrderTables(Arrays.asList(
                createOrderTable(1L, 1L, false),
                createOrderTable(2L, 2L, false)
        ));
        when(mockOrderTableDao.findAllByIdIn(any())).thenReturn(tableGroup.getOrderTables());
        assertThatThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        when(mockOrderTableDao.findAllByTableGroupId(any())).thenReturn(tableGroup.getOrderTables());
        when(mockOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
        ).thenReturn(false);
        tableGroupService.ungroup(tableGroup.getId());

        ArgumentCaptor<OrderTable> argument = ArgumentCaptor.forClass(OrderTable.class);
        verify(mockOrderTableDao, times(2)).save(argument.capture());
        for (OrderTable orderTable : argument.getAllValues()) {
            assertThat(orderTable.getTableGroupId()).isNull();
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @DisplayName("COOKING, MEAL 상태의 테이블 그룹은 해제할 수 없다.")
    @Test
    void ungroupWithInvalidStatusOrderTables() {
        when(mockOrderTableDao.findAllByTableGroupId(any())).thenReturn(tableGroup.getOrderTables());
        when(mockOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
        ).thenReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }
}
