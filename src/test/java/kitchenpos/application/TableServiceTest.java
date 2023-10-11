package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TableServiceTest {

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

}
