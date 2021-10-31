package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TableGroupServiceTest extends ServiceTest {
    @Mock
    private JdbcTemplateOrderDao orderDao;
    @Mock
    private JdbcTemplateTableGroupDao tableGroupDao;
    @Mock
    private JdbcTemplateOrderTableDao orderTableDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 할 수 있다")
    @Test
    void create() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(
                Arrays.asList(OrderTableFixture.notGroupedTable(), OrderTableFixture.notGroupedTable()));
        when(tableGroupDao.save(any())).thenReturn(TableGroupFixture.tableGroup());

        tableGroupService.create(TableGroupFixture.tableGroup());
    }

    @DisplayName("단체 지정시 주문 테이블이 둘 이상 존재해야 한다")
    @Test
    void createDoubleOrderTable() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(
                Arrays.asList(OrderTableFixture.notGroupedTable()));

        assertThatThrownBy(() -> tableGroupService.create(TableGroupFixture.tableGroup())).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정시 주문 테이블이 모두 저장되어 있어야 한다")
    @Test
    void createSavedOrderTable() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(
                Arrays.asList());

        assertThatThrownBy(() -> tableGroupService.create(TableGroupFixture.tableGroup())).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정시 주문 테이블의 그룹이 지정되지 않은 상태여야 한다")
    @Test
    void createNullOrderTable() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(
                Arrays.asList(OrderTableFixture.orderTable(), OrderTableFixture.orderTable()));

        assertThatThrownBy(() -> tableGroupService.create(TableGroupFixture.tableGroup())).
                isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void unGroup() {

        tableGroupService.ungroup(0L);
    }

    @DisplayName("단체 지정 취소시 요리중이거나 식사중인 테이블은 취소할 수 없다")
    @Test
    void unGroupExists() {
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(0L)).
                isInstanceOf(IllegalArgumentException.class);
    }
}
