package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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

    @Nested
    class 테이블_인원_변경 {

        @Test
        void 성공() {
            //given
            OrderTable 테이블 = empty가_아닌_테이블_조회();
            테이블.setNumberOfGuests(9);

            //when
            OrderTable 변경된_테이블 = tableService.changeNumberOfGuests(테이블.getId(), 테이블);

            //then
            assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(9);
        }

        private OrderTable empty가_아닌_테이블_조회() {
            OrderTable 테이블 = orderTableDao.findAll().get(0);
            테이블.setEmpty(false);
            orderTableDao.save(테이블);
            return 테이블;
        }

        @Test
        void 인원이_음수면_예외가_발생한다() {
            //given
            OrderTable 테이블 = orderTableDao.findAll().get(0);
            테이블.setNumberOfGuests(-1);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블.getId(), 테이블))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_예외가_발생한다() {
            //given
            OrderTable 테이블 = new OrderTable();

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(10000000L, 테이블))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_empty이면_예외가_발생한다() {
            //given
            OrderTable 테이블 = orderTableDao.findAll().get(0);
            테이블.setEmpty(true);
            orderTableDao.save(테이블);
            테이블.setNumberOfGuests(9);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블.getId(), 테이블))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
