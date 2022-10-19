package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Nested
    class create_메소드는 {

        @Nested
        class 생성할_주문테이블을_입력받는_경우 {

            private final OrderTable orderTable = new OrderTable(0, true);

            @Test
            void 주문테이블을_저장하고_반환한다() {
                OrderTable actual = tableService.create(orderTable);

                assertThat(actual).isNotNull();
            }
        }
    }

    @Nested
    class list_메소드는 {

        @Nested
        class 요청이_들어오는_경우 {

            @Test
            void 주문테이블목록을_반환한다() {
                List<OrderTable> actual = tableService.list();

                assertThat(actual).hasSize(8);
            }
        }
    }
}
