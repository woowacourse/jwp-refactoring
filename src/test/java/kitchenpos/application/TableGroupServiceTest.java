package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableDao.save(new OrderTable(0, true));
        orderTable2 = orderTableDao.save(new OrderTable(0, true));
        orderTable3 = orderTableDao.save(new OrderTable(0, true));
    }

    @Test
    void create() {
        TableGroup tableGroup = tableGroupService.create(
                new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2, orderTable3)));

        assertThat(tableGroup.getId()).isNotNull();
    }

    @Test
    void ungroup() {
        TableGroup tableGroup = tableGroupService.create(
                new TableGroup(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2, orderTable3)));

        tableGroupService.ungroup(tableGroup.getId());
    }
}
