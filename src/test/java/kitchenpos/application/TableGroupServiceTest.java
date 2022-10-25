package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);

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
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블이 있는 경우 예외가 발생한다.")
    @Test
    void createWithNotExistOrderTable() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, new OrderTable());

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있지 않으면 예외가 발생한다.")
    @Test
    void createWithNotEmptyOrderTable() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, false));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, false));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
    @Test
    void createWithOrderTableExistingTableGroup() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);
        tableGroupService.create(new TableGroup(orderTables));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> createOrderTable(OrderTable... orderTables) {
        return new ArrayList<>(Arrays.asList(orderTables));
    }
}
