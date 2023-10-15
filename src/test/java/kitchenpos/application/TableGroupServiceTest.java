package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("테이블 그룹 테스트")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 1, true);
        final OrderTable orderTable2 = createOrderTable(2L, 1, true);

        // when
        final TableGroup actual = createTableGroup(List.of(orderTable1, orderTable2));

        // then
        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @DisplayName("2개 미만의 테이블 그룹 생성에 실패한다")
    @Test
    void create_Fail() {
        // given
        final OrderTable orderTable = createOrderTable(1L, 1, true);
        final TableGroup tableGroup = createTableGroup(List.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어 있지 않을 경우 그룹생성에 실패한다")
    @Test
    void create_FailEmptyTable() {
        //
        final OrderTable orderTable1 = createOrderTable(1L, null, null);
        final OrderTable orderTable2 = createOrderTable(5L, null, null);
        final TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));
        // when
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3, true);
        final OrderTable orderTable2 = createOrderTable(2L, 3, true);
        final TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));
        final Long tableGroupId = tableGroupService.create(tableGroup).getId();

        // when
        tableGroupService.ungroup(tableGroupId);
        final List<OrderTable> actual = tableService.list();

        // then
        assertSoftly(
                softly -> {
                    actual.stream()
                            .filter(orderTable -> orderTable.getId().equals(1L) || orderTable.getId().equals(2L))
                            .forEach(orderTable -> softly.assertThat(orderTable.getTableGroupId()).isNull());
                }
        );
    }

    @DisplayName("조리 중이거나 먹는 중인 테이블이면 그룹 해제에 실패한다")
    @ParameterizedTest(name = "{0} 중인 테이블 상태 변경시 실패한다")
    @MethodSource("statusAndIdProvider")
    void ungroup_FailNonExistTable(final String name, final Long id, final Class exception) {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3, true);
        final OrderTable orderTable2 = createOrderTable(id, 3, true);
        final TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));
        final Long tableGroupId = tableGroupService.create(tableGroup).getId();
        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(exception);
    }

    private TableGroup createTableGroup(List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
