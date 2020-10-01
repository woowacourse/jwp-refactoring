package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @DisplayName("테이블 생성 메서드 테스트")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("테이블 목록 조회 기능 테스트")
    @Test
    void list() {
        List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(8);
    }
}
