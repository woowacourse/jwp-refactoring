package kitchenpos.application;

import static kitchenpos.DomainFixture.getEmptyTable;
import static kitchenpos.DomainFixture.getMenuGroup;
import static kitchenpos.DomainFixture.getNotEmptyTable;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getOrderCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        final OrderTable table = getEmptyTable();

        final OrderTable savedTable = 테이블_등록(table);

        assertAll(
                () -> assertThat(savedTable.getId()).isNotNull(),
                () -> assertThat(savedTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        테이블_등록(getEmptyTable());

        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(1);
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = 테이블_등록(getNotEmptyTable(0));
        final OrderTable emptyTable = getEmptyTable();

        final OrderTable changedTable = tableService.changeEmpty(orderTable.getId(), emptyTable);

        assertAll(
                () -> assertThat(changedTable.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(changedTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("빈 테이블로 변경한다. - 존재하지 않는 테이블이면 예외를 반환한다.")
    @Test
    void changeEmpty_exception_noSuchTable() {
        final OrderTable emptyTable = getEmptyTable();

        assertThatThrownBy(() -> tableService.changeEmpty(null, emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경한다. - 단체 지정에 포함된 테이블이면 예외를 반환한다.")
    @Test
    void changeEmpty_exception_alreadyInTableGroup() {
        final OrderTable orderTable = 테이블_등록(getEmptyTable());
        final OrderTable anotherTable = 테이블_등록(getEmptyTable());

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable, 테이블_등록(anotherTable)));
        tableGroupService.create(tableGroup);

        final OrderTable emptyTable = 테이블_등록(getEmptyTable());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경한다. - 주문 상태가 COOKING이거나 MEAL이면 예외를 반환한다.")
    @Test
    void changeEmpty_exception_orderStatusIsCookingOrMeal() {
        final OrderTable savedTable = 테이블_등록(getNotEmptyTable(0));
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final Menu menu = 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));
        주문_등록(getOrderCreateRequest(savedTable.getId(), menu.getId()));

        final OrderTable emptyTable = getEmptyTable();

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable savedTable = 테이블_등록(getNotEmptyTable(0));
        final OrderTable changeTable = getNotEmptyTable(5);

        final OrderTable changedTable = tableService.changeNumberOfGuests(savedTable.getId(), changeTable);

        assertAll(
                () -> assertThat(changedTable.getId()).isEqualTo(savedTable.getId()),
                () -> assertThat(changedTable.getNumberOfGuests()).isEqualTo(changeTable.getNumberOfGuests()),
                () -> assertThat(changedTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("방문한 손님 수를 변경한다. - 손님의 수가 0보다 작으면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_exception_numberOfGuestsIsLessThanZero() {
        final OrderTable savedTable = 테이블_등록(getNotEmptyTable(0));
        final OrderTable changeTable = getNotEmptyTable(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다. - 존재하지 않는 주문 테이블이면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_exception_noSuchTable() {
        final OrderTable changeTable = getNotEmptyTable(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(null, changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다. - 빈 테이블이면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_exception_tableIsEmpty() {
        final OrderTable savedTable = 테이블_등록(getEmptyTable());
        final OrderTable changeTable = getNotEmptyTable(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
