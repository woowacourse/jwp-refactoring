package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;


    @Test
    void 테이블_등록() {
        // given
        OrderTable orderTable = OrderTableFixture.create();

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void 테이블_목록_조회() {
        // given
        OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.create());
        OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.create());

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(orderTable1, orderTable2));
    }

    @Nested
    class 주문_테이블을_빈_테이블로_변경할_때 {

        @Test
        void success() {

        }

        @Test
        void 단체_테이블에_소속되어_있으면_실패() {

        }

        @Test
        void 주문_상태가_조리중이거나_식사_중이면_실패() {

        }


    }

    @Nested
    class 주문_테이블의_고객_수를_변경할_때 {


        @Test
        void success() {

        }

        @Test
        void 변경할_고객의_수가_0_미만이면_실패() {

        }

        @Test
        void 등록되지_않은_주문_테이블이면_실패() {

        }

        @Test
        void 주문_테이블이_빈_테이블이면_실패() {

        }

    }

}
