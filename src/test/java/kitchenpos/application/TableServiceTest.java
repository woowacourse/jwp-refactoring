package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class TableServiceTest {

    private final TableService tableService;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    @Autowired
    public TableServiceTest(final TableService tableService, final OrderDao orderDao,
                            final OrderTableDao orderTableDao) {
        this.tableService = tableService;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @DisplayName("주문 테이블을 추가한다")
    @Test
    void create() {
        final var expected = new OrderTable(null, 1, false);
        final var actual = tableService.create(expected);

        assertThat(actual.getId()).isPositive();
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @DisplayName("주문 테이블을 전체 조회한다")
    @Test
    void list() {
        final List<OrderTable> expected = Stream.of(
                        new OrderTable(null, 1, false),
                        new OrderTable(null, 5, false),
                        new OrderTable(null, 10, true))
                .map(orderTableDao::save)
                .collect(Collectors.toUnmodifiableList());
        final List<OrderTable> actual = tableService.list();

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("주문 테이블을 비운다")
    @Test
    void changeEmpty() {
        final var expected = orderTableDao.save(new OrderTable(null,1,false));
        expected.setEmpty(true);

        final var actual = tableService.changeEmpty(expected.getId(), expected);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("존재하는 주문 테이블이어야만 비울 수 있다")
    @Test
    void changeEmptyWithNonExistOrderTable() {
        final var nonExistOrderTableId = 0L;

        final var orderTable = new OrderTable(null,1,false);

        assertThatThrownBy(() -> tableService.changeEmpty(nonExistOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("단체 지정되어 있는 주문 테이블은 임의로 비울 수 없다")
    @Test
    void changeEmptyWithAssignedTableGroup() {
        final var tableGroupId = 1L;

        final var orderTable = orderTableDao.save(new OrderTable(tableGroupId, 1, false));
        orderTable.setEmpty(true);

        final var orderTableId = orderTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 테이블입니다.");
    }

    @DisplayName("계산이 완료된 테이블이어야만 비울 수 있다")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyWithNotCompletedOrderTable(final String orderStatus) {
        final var orderTable = orderTableDao.save(new OrderTable(null, 1, false));
        final var orderTableId = orderTable.getId();

        orderDao.save(new Order(orderTableId, orderStatus, LocalDateTime.now(), Collections.emptyList()));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산이 완료되지 않은 테이블입니다.");
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void changeNumberOfGuests(final int expectedNumberOfGuests) {
        final var orderTable = orderTableDao.save(new OrderTable(null, 1, false));

        orderTable.setNumberOfGuests(expectedNumberOfGuests);
        tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        final var actual = orderTable.getNumberOfGuests();
        assertThat(actual).isEqualTo(expectedNumberOfGuests);
    }

    @DisplayName("주문 테이블의 손님 수는 음수로 변경할 수 없다")
    @Test
    void changeNumberOfGuestsWithNegative() {
        final var negativeNumberOfGuests = -1;

        final var orderTable = new OrderTable(null, 1, false);
        final var savedOrderTable = orderTableDao.save(orderTable);

        orderTable.setNumberOfGuests(negativeNumberOfGuests);

        final var orderTableId = savedOrderTable.getId();
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수가 될 수 없습니다.");
    }

    @DisplayName("존재하는 주문 테이블이어야만 손님 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuestsWithNonExistOrderTable() {
        final var nonExistOrderTableId = 0L;

        final var orderTable = orderTableDao.save(new OrderTable(null, 1, false));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(nonExistOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("비어있는 주문 테이블이어야만 손님 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuestsWithEmptyOrderTable() {
        final var isEmpty = true;

        final var orderTable = new OrderTable(null, 1, isEmpty);
        final var savedOrderTable = orderTableDao.save(orderTable);

        final var orderTableId = savedOrderTable.getId();
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있습니다.");
    }
}