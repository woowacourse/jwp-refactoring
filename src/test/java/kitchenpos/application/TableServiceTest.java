package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        OrderTable table1 = givenTable(0, true);
        OrderTable table2 = givenTable(0, true);

        List<OrderTable> tables = tableService.list();

        assertThat(tables).extracting(OrderTable::getId, OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .containsExactlyInAnyOrder(
                        tuple(table1.getId(), 0, true),
                        tuple(table2.getId(), 0, true)
                );
    }

    @DisplayName("테이블의 주문 가능 여부(empty)를 수정한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) {
        OrderTable table = givenTable(0, true);

        OrderTable updateTable = new OrderTable();
        updateTable.setEmpty(empty);
        OrderTable updatedTable = tableService.changeEmpty(table.getId(), updateTable);

        List<OrderTable> tables = tableService.list();
        OrderTable foundTable = tables.stream()
                .filter(t -> updatedTable.getId().equals(t.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(foundTable).extracting("empty")
                .isEqualTo(empty);
    }

    private OrderTable givenTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return tableService.create(orderTable);
    }
}
