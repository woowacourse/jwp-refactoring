package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.*;
import static kitchenpos.fixture.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("2 개 이상의 빈 테이블을 단체로 지정할 수 있다.")
    @Test
    void createTest() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(ORDER_TABLES);
        when(tableGroupDao.save(any())).thenReturn(TABLE_GROUP);

        TableGroup tableGroup = tableGroupService.create(TABLE_GROUP);
        assertAll(
                () -> assertThat(tableGroup.getId()).isEqualTo(1L),
                () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(2),
                () -> assertThat(tableGroup.getOrderTables().get(0).getId()).isEqualTo(1L),
                () -> assertThat(tableGroup.getOrderTables().get(1).getId()).isEqualTo(2L)
        );
    }

    @DisplayName("2 개 미만의 빈 테이블을 단체로 지정할 수 있다.")
    @Test
    void createTest_when_OneOrderTable() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(
                Collections.singletonList(ORDER_TABLE1));

        assertThatThrownBy(() -> tableGroupService.create(TABLE_GROUP))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정은 중복될 수 없다.")
    @Test
    void createTest_when_OrderTable() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(ORDER_TABLES);
        when(tableGroupDao.save(any())).thenReturn(TABLE_GROUP);

        tableGroupService.create(TABLE_GROUP);

        assertThatThrownBy(() -> tableGroupService.create(TABLE_GROUP))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해지할 수 있다.")
    @Test
    void ungroupTest() {
        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(ORDER_TABLES);

        tableGroupService.ungroup(TABLE_GROUP.getId());

        assertAll(
                () -> assertThat(ORDER_TABLES.get(0).getTableGroupId()).isNull(),
                () -> assertThat(ORDER_TABLES.get(1).getTableGroupId()).isNull(),
                () -> assertThat(ORDER_TABLES.get(0).isEmpty()).isFalse(),
                () -> assertThat(ORDER_TABLES.get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
    @Test
    void notUngroupTest_when_orderStatusIsMEALAndCooking() {
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(
                true);

        assertThatThrownBy(() -> tableGroupService.ungroup(TABLE_GROUP.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}