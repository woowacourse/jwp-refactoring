package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        TableGroup tableGroup = createTableGroup();

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup).usingRecursiveComparison()
            .ignoringFields("id", "createdDate")
            .isEqualTo(tableGroup);
    }

    @Test
    @DisplayName("주문 테이블이 빈 경우 예외를 던진다.")
    void create_empty_table() {
        // given
        TableGroup tableGroup = createTableGroup(List.of());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블크기가 2 미만인 경우 예외를 던진다.")
    void create_table_under_size2() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(1, true));
        TableGroup tableGroup = createTableGroup(List.of(orderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 속한 OrderTable 을 삭제한다.")
    void ungroup() {
        // given
        TableGroup tableGroup = createTableGroup();

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        Long tableGroupId = savedTableGroup.getId();
        tableGroupService.ungroup(tableGroupId);
        TableGroup foundTableGroup = tableGroupDao.findById(tableGroupId).orElseThrow();

        // then
        assertThat(foundTableGroup.getOrderTables()).isNull();
    }

    private TableGroup createTableGroup() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        OrderTable savedOrderTable1 = tableService.create(orderTable1);
        OrderTable savedOrderTable2 = tableService.create(orderTable2);
        return createTableGroup(List.of(savedOrderTable1, savedOrderTable2));
    }

    private TableGroup createTableGroup(List<OrderTable> tables) {
        return new TableGroup(tables);
    }
}
