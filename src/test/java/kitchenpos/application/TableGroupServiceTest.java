package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("단체 테이블 등록 시 지정된 빈 테이블들을 주문 테이블로 변경해 저장한다.")
    void 단체_지정_성공_주문_테이블로_변경() {
        // given
        final List<OrderTable> existingTables = tableService.list();
        final long emptyCountBefore = countEmpty(existingTables);

        // when
        final TableGroup saved = createTableGroup(existingTables);

        // then
        final long emptyCountAfter = countEmpty(saved.getOrderTables());
        assertThat(emptyCountBefore).isEqualTo(existingTables.size());
        assertThat(emptyCountAfter).isZero();
    }

    private TableGroup createTableGroup(final List<OrderTable> tables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(tables);

        return tableGroupService.create(tableGroup);
    }

    private long countEmpty(final List<OrderTable> tables) {
        return tables.stream()
                .filter(OrderTable::isEmpty)
                .count();
    }

    @Test
    @DisplayName("기존에 존재하는 테이블만 단체로 지정할 수 있다.")
    void 단체_지정_실패_존재하지_않는_테이블() {
        // given
        final OrderTable existingTable = tableService.list().get(0);
        final OrderTable newTable = new OrderTable();
        newTable.setEmpty(true);

        // when
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(existingTable, newTable));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소속된 단체가 없는 테이블만 단체로 지정할 수 있다.")
    void 단체_지정_실패_이미_소속_단체가_있는_테이블() {
        // given
        final List<OrderTable> tablesWithGroup = tableService.list();
        createTableGroup(tablesWithGroup);

        // when
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(tablesWithGroup);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블만 단체로 지정할 수 있다.")
    void 단체_지정_실패_주문_테이블() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(createNotEmptyTables(2));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> createNotEmptyTables(int count) {
        List<OrderTable> tables = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final OrderTable table = new OrderTable();
            table.setEmpty(false);
            tables.add(tableService.create(table));
        }
        return tables;
    }

}
