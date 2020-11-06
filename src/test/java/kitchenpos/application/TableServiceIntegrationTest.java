package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.OrderTable;

class TableServiceIntegrationTest extends ServiceIntegrationTest {
    @Autowired
    TableService tableService;

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        OrderTable persist = tableService.create(orderTable);

        assertThat(persist).isEqualToIgnoringNullFields(orderTable);
    }

    @DisplayName("테이블 전체를 조회한다.")
    @Test
    void list() {
        List<OrderTable> orderTables = tableService.list();

        assertAll(
            () -> assertThat(orderTables).hasSize(8),
            () -> assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(orderTables.get(0).isEmpty()).isEqualTo(true),
            () -> assertThat(orderTables.get(1).getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(orderTables.get(1).isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("테이블의 빈 테이블 여부를 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable changedTable = tableService.changeEmpty(1L, getOrderTableNotEmpty());

        assertThat(changedTable.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        int numberOfGuests = 4;
        OrderTable changedTable = tableService.changeNumberOfGuests(2L, getOrderTableWithGuests(numberOfGuests));

        assertThat(changedTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}