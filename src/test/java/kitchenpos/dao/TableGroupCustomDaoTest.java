package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.ordertable.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import(TableGroupCustomDao.class)
class TableGroupCustomDaoTest {

    @Autowired
    private TableGroupCustomDao tableGroupCustomDao;

    @Test
    @DisplayName("OrderTables에 대한 persistance cascade를 지원한다. TableGroup 저장 시 TableGroup 안에 있는 OrderTables에 대한 저장도 함께 한다.")
    void cascadeSaving() {
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(6), true);
        final OrderTable orderTable2 = new OrderTable(new NumberOfGuests(6), true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2));

        final TableGroup savedTableGroup = tableGroupCustomDao.save(tableGroup);

        assertAll(() -> {
            assertThat(savedTableGroup.getId()).as("tableGroup이 정상 저장되었음은 물론이고,").isNotNull();
            for (OrderTable savedOrderTable : savedTableGroup.getOrderTables()) {
                assertThat(savedOrderTable.getId()).as("orderTable도 함께 저장되어 식별자를 갖게 된다.").isNotNull();
                assertThat(savedOrderTable.getTableGroupId())
                        .as("orderTable의 외래키로 tableGroup의 식별자를 갖게 된다.")
                        .isEqualTo(savedTableGroup.getId());
            }
        });
    }
}
