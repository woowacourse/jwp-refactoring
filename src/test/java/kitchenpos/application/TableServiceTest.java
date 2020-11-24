package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

class TableServiceTest extends ServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(null, 1L, 2, false);

        OrderTable saved = tableService.create(orderTable);

        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void list() {
        OrderTable orderTable = new OrderTable();
        orderTableDao.save(orderTable);

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(1);
    }

}