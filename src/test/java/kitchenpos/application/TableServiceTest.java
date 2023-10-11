package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;


    @Test
    void 테이블의_주문을_생성할_수_있다() {
        //given
        OrderTable 주문 = new OrderTable();

        //when
        OrderTable 생성된_주문 = tableService.create(주문);

        //then
        assertThat(생성된_주문.getId()).isNotNull();
    }

    @Test
    void 테이블_목록을_조회할_수_있다() {
        //given
        OrderTable 생성된_테이블 = orderTableDao.save(new OrderTable());

        //when
        List<OrderTable> actual = tableService.list();

        //then
        assertThat(actual)
                .extracting(OrderTable::getId)
                .contains(생성된_테이블.getId());
    }

}
