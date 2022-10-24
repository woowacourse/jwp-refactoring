package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);
        orderTable.setEmpty(false);
    }

    @Test
    @DisplayName("테이블을 생성한다")
    void create() {
        final OrderTable createOrderTable = tableService.create(orderTable);

        assertThat(createOrderTable.getNumberOfGuests())
                .isEqualTo(5);
    }

    @Test
    @DisplayName("테이블 전체를 조회한다")
    void list() {
        assertThat(tableService.list())
                .hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("테이블을 비운다")
    void changeEmpty() {
        final OrderTable createOrderTable = tableService.create(orderTable);

        final OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);

        final OrderTable actual = tableService.changeEmpty(createOrderTable.getId(), emptyOrderTable);

        assertThat(actual.isEmpty())
                .isTrue();
    }

    @Test
    @DisplayName("손님 수를 변경한다")
    void changeNumberOfGuests() {
        final OrderTable createOrderTable = tableService.create(orderTable);

        final OrderTable expected = new OrderTable();
        expected.setEmpty(false);
        expected.setNumberOfGuests(6);

        final OrderTable actual = tableService.changeNumberOfGuests(createOrderTable.getId(), expected);

        assertThat(actual.getNumberOfGuests())
                .isEqualTo(6);
    }

    @Test
    @DisplayName("손님 수를 0 미만으로 변경하면 예외를 반환한다")
    void changeNumberOfGuests_negativeNumberException() {
        final OrderTable createOrderTable = tableService.create(orderTable);

        final OrderTable expected = new OrderTable();
        expected.setEmpty(false);
        expected.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createOrderTable.getId(), expected))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
