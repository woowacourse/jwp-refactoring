package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.CreateTableRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class TableServiceTest extends ServiceTest {


    @Autowired
    private TableService tableService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void 테이블을_생성할_수_있다() {
        //given
        CreateTableRequest 테이블_생성_요청 = new CreateTableRequest();

        //when
        OrderTable 생성된_주문 = tableService.create(테이블_생성_요청);

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
            OrderTable 테이블_엔티티 = new OrderTable();
            OrderTable 테이블 = orderTableDao.save(테이블_엔티티);
            Long 삭제된_테이블_아이디 = 테이블.getId();
            테이블_삭제(테이블);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(삭제된_테이블_아이디, 테이블_엔티티))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private void 테이블_삭제(OrderTable 테이블) {
            jdbcTemplate.execute("delete from order_table where id = " + 테이블.getId());
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

    @Nested
    class 테이블_비우기 {

        @Test
        void 성공() {
            //given
            OrderTable 테이블 = new OrderTable();
            테이블.setEmpty(false);
            OrderTable 생성된_테이블 = orderTableDao.save(테이블);
            생성된_테이블.setEmpty(true);

            //when
            OrderTable 변경된_테이블 = tableService.changeEmpty(생성된_테이블.getId(), 생성된_테이블);

            //then
            assertThat(변경된_테이블.isEmpty()).isTrue();
        }

        @Test
        void 테이블에_COMPLETION_상태가_아닌_주문이_있으면_예외가_발생한다() {
            //given
            OrderTable 테이블 = new OrderTable();
            테이블.setEmpty(false);
            OrderTable 생성된_테이블 = orderTableDao.save(테이블);
            주문만들기(생성된_테이블);

            생성된_테이블.setEmpty(true);

            //expect
            assertThatThrownBy(() -> tableService.changeEmpty(생성된_테이블.getId(), 생성된_테이블))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private void 주문만들기(final OrderTable 생성된_테이블) {
            OrderLineItem 주문상품 = 주문_상품_만들기();
            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());
            주문.setOrderTableId(생성된_테이블.getId());
            주문.setOrderedTime(now());
            orderDao.save(주문);
        }

        private OrderLineItem 주문_상품_만들기() {
            final var 메뉴 = menuDao.findAll().get(0);
            OrderLineItem 주문상품 = new OrderLineItem();
            주문상품.setMenuId(메뉴.getId());
            주문상품.setQuantity(1);
            return 주문상품;
        }

        @Test
        void 그룹에속한_테이블이면_예외가_발생한다() {
            //given
            TableGroup 그룹 = new TableGroup();
            그룹.setCreatedDate(now());
            TableGroup 생성된_그룹 = tableGroupDao.save(그룹);
            OrderTable 테이블 = new OrderTable();
            테이블.setEmpty(false);
            테이블.setTableGroupId(생성된_그룹.getId());
            OrderTable 생성된_테이블 = orderTableDao.save(테이블);

            //expect
            assertThatThrownBy(() -> tableService.changeEmpty(생성된_테이블.getId(), 생성된_테이블))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
