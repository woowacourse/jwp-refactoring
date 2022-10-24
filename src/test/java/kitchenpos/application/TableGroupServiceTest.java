package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("테이블 그룹을 등록할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        // when
        final TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual.getOrderTables())
                .usingElementComparatorOnFields("id")
                .containsExactly(savedTable1, savedTable2, savedTable3);
    }

    @Test
    @DisplayName("2개 미만의 테이블을 테이블 그룹으로 등록할 수 없다.")
    void create_ExceptionOrderTablesLowerThanTwo() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionNotSavedOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, orderTable3);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않는 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionEmptyOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(false, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 테이블 그룹에 속한 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionAlreadyCreatedTableGroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup1 = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup tableGroup2 = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        tableGroupService.create(tableGroup1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup() {
    }
}
