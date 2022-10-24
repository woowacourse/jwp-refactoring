package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderTable;
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

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        final var table = new OrderTable(2);

        final var result = tableService.create(table);

        assertThat(table).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(result);
    }

    @DisplayName("등록된 주문 테이블의 빈 테이블 여부 상태 변경")
    @Test
    void changeEmpty() {
        final var table = tableService.create(new OrderTable(2));

        final var updatedTable = new OrderTable(2);
        updatedTable.setEmpty(true);

        final var result = tableService.changeEmpty(table.getId(), updatedTable);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(table.getId()),
                () -> assertThat(result.isEmpty()).isEqualTo(updatedTable.isEmpty())
        );
    }

    @DisplayName("단체 지정된 주문 테이블의 빈 테이블 여부 상태 변경 시 예외 발생")
    @Test
    void changeEmpty_hasTableGroup_throwsException() {
        final var table = new OrderTable(2);
        table.setTableGroupId(1L);
        tableService.create(table);

        final var updatedTable = new OrderTable(2);
        updatedTable.setEmpty(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("등록된 주문 테이블의 고객 수 변경")
    @Test
    void changeNumberOfGuests() {
        final var table = tableService.create(new OrderTable(2));

        final var updatedTable = new OrderTable(3);

        final var result = tableService.changeNumberOfGuests(table.getId(), updatedTable);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(table.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(updatedTable.getNumberOfGuests())
        );
    }

    @DisplayName("등록된 주문 테이블의 고객 수를 0 미만으로 변경 시 예외 발생")
    @Test
    void changeNumberOfGuests_toUnderZero_throwsException() {
        final var table = tableService.create(new OrderTable(2));

        final var updatedTable = new OrderTable(-1);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블의 상태가 빈 테이블 일 때, 고객 수 변경 시 예외 발생")
    @Test
    void changeNumberOfGuests_tableIsEmptyTrue_throwsException() {
        final var table = new OrderTable(2);
        table.setEmpty(true);
        tableService.create(table);

        final var updatedTable = new OrderTable(3);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(table.getId(), updatedTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 모든 주문 테이블 목록 조회")
    @Test
    void list() {
        final var existingTables = tableService.list();

        final var twoPeopleTable = new OrderTable(2);
        final var fivePeopleTable = new OrderTable(5);

        tableService.create(twoPeopleTable);
        tableService.create(fivePeopleTable);

        final var result = tableService.list();
        final var expected = List.of(twoPeopleTable, fivePeopleTable);

        assertThat(result.size()).isEqualTo(existingTables.size() + expected.size());
    }
}
