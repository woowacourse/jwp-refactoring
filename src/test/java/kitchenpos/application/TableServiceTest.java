package kitchenpos.application;

import static kitchenpos.application.DomainFixture.getEmptyTable;
import static kitchenpos.application.DomainFixture.getNotEmptyTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @MockBean
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private final TableService tableService = new TableService(orderDao, orderTableDao);

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        final OrderTable table = getEmptyTable();

        final OrderTable savedTable = tableService.create(table);

        assertAll(
                () -> assertThat(savedTable.getId()).isNotNull(),
                () -> assertThat(savedTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(8);
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = tableService.create(getNotEmptyTable(0));
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
        final OrderTable orderTable = tableService.create(getEmptyTable());
        final OrderTable anotherTable = tableService.create(getEmptyTable());

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable, tableService.create(anotherTable)));
        tableGroupService.create(tableGroup);

        final OrderTable emptyTable = tableService.create(getEmptyTable());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경한다. - 주문 상태가 COOKING이거나 MEAL이면 예외를 반환한다.")
    @Test
    void changeEmpty_exception_orderStatusIsCookingOrMeal() {
        final OrderTable savedTable = tableService.create(getNotEmptyTable(0));

        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                savedTable.getId(), List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        final OrderTable emptyTable = getEmptyTable();

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable savedTable = tableService.create(getNotEmptyTable(0));
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
        final OrderTable savedTable = tableService.create(getNotEmptyTable(0));
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
        final OrderTable savedTable = tableService.create(getEmptyTable());
        final OrderTable changeTable = getNotEmptyTable(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
