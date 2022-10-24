package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void create() {
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable saved = orderTableDao.save(orderTable);
        OrderTable saved1 = orderTableDao.save(orderTable1);
        tableGroup.setOrderTables(Arrays.asList(saved, saved1));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(tableGroupDao.findById(savedTableGroup.getId())).isPresent();
    }

    @Test
    void create_tableGroupCountException() {
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable saved = orderTableDao.save(orderTable);
        tableGroup.setOrderTables(Collections.singletonList(saved));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_orderTableSettingException() {
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable();
        OrderTable orderTable1 = new OrderTable();
        OrderTable saved = orderTableDao.save(orderTable);
        OrderTable saved1 = orderTableDao.save(orderTable1);
        tableGroup.setOrderTables(Arrays.asList(saved, saved1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable saved = orderTableDao.save(orderTable);
        OrderTable saved1 = orderTableDao.save(orderTable1);
        tableGroup.setOrderTables(Arrays.asList(saved, saved1));

        tableGroupService.create(tableGroup);
        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTableDao.findById(saved.getId()).get().isEmpty()).isFalse();
    }
}
