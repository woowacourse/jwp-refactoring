package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문_테이블_생성_성공() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        Optional<OrderTable> actual = orderTableDao.findById(savedOrderTable.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 주문_테이블_목록_조회() {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        List<OrderTable> expected = Collections.singletonList(orderTable);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 주문_테이블_비어있는_상태_변경() {
        // given
        OrderTable orderTable = 주문_테이블_생성();
        boolean changedEmpty = !orderTable.isEmpty();
        orderTable.setEmpty(changedEmpty);

        Long orderTableId = orderTable.getId();

        // when
        tableService.changeEmpty(orderTableId, orderTable);

        // then
        Optional<OrderTable> actual = orderTableDao.findById(orderTableId);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().isEmpty()).isEqualTo(changedEmpty);
    }

    private OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);
        return orderTableDao.save(orderTable);
    }
}
