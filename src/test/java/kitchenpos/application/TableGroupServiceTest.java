package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void create() {
        List<Long> orderTableIds = createOrderTableIds(1L, 2L);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);

        TableGroup tableGroup = tableGroupService.create(new TableGroup(orderTables));

        assertThat(tableGroup).isNotNull();
    }

    @DisplayName("테이블 그룹에 등록된 테이블이 없으면 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 등록된 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void createWithLessThanTwoOrderTable() {
        List<Long> orderTableIds = createOrderTableIds(1L);

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블이 있는 경우 예외가 발생한다.")
    @Test
    void createWithNotExistOrderTable() {
        List<Long> orderTableIds = createOrderTableIds(9999L, 999L);

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있지 않으면 예외가 발생한다.")
    @Test
    void createWithNotEmptyOrderTable() {
        List<Long> orderTableIds = createOrderTableIds(1L, 2L);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        OrderTable firstOrderTable = orderTables.get(0);
        firstOrderTable.setEmpty(false);
        orderTableDao.save(firstOrderTable);

        List<OrderTable> foundOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(foundOrderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
    @Test
    void createWithOrderTableExistingTableGroup() {
        List<Long> orderTableIds = createOrderTableIds(1L, 2L);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        tableGroupService.create(new TableGroup(orderTables));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<Long> createOrderTableIds(Long... ids) {
        List<Long> orderTableIds = new ArrayList<>();
        for (Long id : ids) {
            orderTableIds.add(id);
        }
        return orderTableIds;
    }
}
