package kitchenpos.application;

import static kitchenpos.DomainFixture.getMenuGroup;
import static kitchenpos.DtoFixture.getEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getNotEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getOrderCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() {
        final OrderTable table1 = 테이블_등록(getEmptyTableCreateRequest());
        final OrderTable table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final TableGroup savedTableGroup = 단체_지정(tableGroup);

        final OrderTable savedTable1 = orderTableDao.findById(table1.getId()).get();
        final OrderTable savedTable2 = orderTableDao.findById(table2.getId()).get();

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isBefore(LocalDateTime.now()),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(savedTable1.getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(savedTable2.getTableGroupId()).isEqualTo(savedTableGroup.getId())
        );
    }

    @DisplayName("단체 지정을 등록한다. - 주문 테이블이 존재하지 않으면 예외를 반환한다.")
    @Test
    void create_exception_noTables() {
        final TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> 단체_지정(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 주문 테이블이 2개보다 적으면 예외를 반환한다.")
    @Test
    void create_exception_tableLessThanTwo() {
        final OrderTable table1 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1));

        assertThatThrownBy(() -> 단체_지정(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 비어있지 않은 테이블이 포함되어 있다면 예외를 반환한다.")
    @Test
    void create_exception_containsNotEmptyTable() {
        final OrderTable table1 = 테이블_등록(getEmptyTableCreateRequest());
        final OrderTable table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroup tableGroup = new TableGroup();
        final OrderTable table3 = 테이블_등록(getNotEmptyTableCreateRequest(0));
        tableGroup.setOrderTables(List.of(table1, table2, table3));

        assertThatThrownBy(() -> 단체_지정(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 이미 다른 단체에 지정된 테이블이 포함되어 있다면 예외를 반환한다.")
    @Test
    void create_exception_containsAlreadyGroupedTable() {
        final OrderTable table1 = 테이블_등록(getEmptyTableCreateRequest());
        final OrderTable table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        단체_지정(tableGroup);

        final OrderTable table3 = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final TableGroup newTableGroup = new TableGroup();
        newTableGroup.setOrderTables(List.of(table1, table3));

        assertThatThrownBy(() -> 단체_지정(newTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해체한다.")
    @Test
    void ungroup() {
        final OrderTable table1 = 테이블_등록(getEmptyTableCreateRequest());
        final OrderTable table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final TableGroup savedTableGroup = 단체_지정(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());

        final OrderTable savedTable1 = orderTableDao.findById(table1.getId()).get();
        final OrderTable savedTable2 = orderTableDao.findById(table2.getId()).get();

        assertAll(
                () -> assertThat(savedTable1.getTableGroupId()).isNull(),
                () -> assertThat(savedTable2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 지정을 해체한다. - 주문 상태가 COOKING이거나 MEAL인 테이블이 존재한다면 예외를 반환한다.")
    @Test
    void list_exception_orderStatusIsCookingOrMeal() {
        final OrderTable table1 = 테이블_등록(getEmptyTableCreateRequest());
        final OrderTable table2 = 테이블_등록(getEmptyTableCreateRequest());
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final Menu menu = 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final TableGroup savedTableGroup = 단체_지정(tableGroup);

        주문_등록(getOrderCreateRequest(table1.getId(), menu.getId()));

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
