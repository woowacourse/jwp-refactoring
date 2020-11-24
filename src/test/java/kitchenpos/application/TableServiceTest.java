package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.OrderTable;

class TableServiceTest extends ServiceTest {
    @Autowired
    private TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(null, 1L, 2, false);

        OrderTable saved = tableService.create(orderTable);

        assertThat(saved.getId()).isNotNull();
    }
}