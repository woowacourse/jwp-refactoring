package kitchenpos.application;

import kitchenpos.TestObjectFactory;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블 그룹 생성 메서드 테스트")
    @Test
    void create() {
        OrderTable orderTable1 = tableService.create(TestObjectFactory.creatOrderTable());
        OrderTable orderTable2 = tableService.create(TestObjectFactory.creatOrderTable());
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = TestObjectFactory.createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedOrderTables.get(0).getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(savedOrderTables.get(1).getTableGroupId()).isEqualTo(savedTableGroup.getId())
        );
    }
}
