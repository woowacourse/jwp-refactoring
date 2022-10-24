package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        OrderTable newOrderTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(newOrderTable.getId()).isNotNull(),
                () -> assertThat(newOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(newOrderTable.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(newOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void list() {
        // when
        List<OrderTable> products = tableService.list();

        // then
        assertThat(products.size()).isEqualTo(8);
    }
}
