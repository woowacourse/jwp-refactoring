package kitchenpos.application;

import static kitchenpos.fixture.MenuFactory.menu;
import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.OrderFactory.order;
import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.ui.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends FakeSpringContext {

    private final TableService tableService = new TableService(orderTableValidator, orderTables, orders);

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        final var request = new OrderTableRequest(2, true);

        final var result = tableService.create(request);

        assertAll(
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(request.isEmpty()).isTrue()
        );
    }

    @DisplayName("등록된 주문 테이블의 빈 테이블 여부 상태 변경")
    @Test
    void changeEmpty() {
        final var table = orderTableDao.save(notEmptyTable(2));

        final var result = tableService.changeEmpty(table.getId(), true);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(table.getId()),
                () -> assertThat(result.isEmpty()).isTrue()
        );
    }

    @DisplayName("등록된 주문 테이블의 주문 상태가 COMPLETION이 아닐 경우, 빈 테이블 상태 변경 시 예외 발생")
    @Test
    void changeEmpty_orderStatusIsNotCompletion_throwsException() {

        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(notEmptyTable(2));

        orderDao.save(order(table, OrderStatus.MEAL, pizzaMenu));

        assertThatThrownBy(
                () -> tableService.changeEmpty(table.getId(), true)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuests() {
        final var table = orderTableDao.save(notEmptyTable(2));
        final var guests = 3;

        final var result = tableService.changeNumberOfGuests(table.getId(), guests);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(table.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(guests)
        );
    }

    @DisplayName("등록된 주문 테이블의 상태가 빈 테이블 일 때, 고객 수 변경 시 예외 발생")
    @Test
    void changeNumberOfGuests_tableIsEmptyTrue_throwsException() {
        final var table = orderTableDao.save(emptyTable(2));

        final var guests = 3;

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(table.getId(), guests)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 모든 주문 테이블 목록 조회")
    @Test
    void list() {
        final var existingTables = tableService.list();

        final var twoPeopleTable = orderTableDao.save(emptyTable(2));
        final var fivePeopleTable = orderTableDao.save(emptyTable(5));

        final var result = tableService.list();
        final var expected = List.of(twoPeopleTable, fivePeopleTable);

        assertThat(result.size()).isEqualTo(existingTables.size() + expected.size());
    }
}
