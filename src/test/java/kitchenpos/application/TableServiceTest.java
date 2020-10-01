package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @DisplayName("테이블 생성 메서드 테스트")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }
}
