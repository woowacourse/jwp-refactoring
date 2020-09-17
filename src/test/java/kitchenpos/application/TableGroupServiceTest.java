package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao tableDao;

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() {
        OrderTable table1 = OrderTable.builder()
            .empty(true)
            .build();
        table1 = tableDao.save(table1);
        OrderTable table2 = OrderTable.builder()
            .empty(true)
            .build();
        table2 = tableDao.save(table2);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1, table2))
            .createdDate(LocalDateTime.now())
            .build();

        TableGroup create = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(create.getId()).isNotNull(),
            () -> assertThat(create.getOrderTables().get(0).isEmpty()).isFalse(),
            () -> assertThat(create.getOrderTables().get(1).isEmpty()).isFalse()
        );
    }
}