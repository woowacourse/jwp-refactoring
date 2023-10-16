package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    class 주문_생성 {

        @Test
        void 성공() {
            //given
            OrderLineItem 주문상품 = 주문_상품_만들기();

            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());

            OrderTable 테이블 = 비어있지_않은_테이블_생성();
            주문.setOrderTableId(테이블.getId());

            //when
            Order 실제주문 = orderService.create(주문);

            //then
            assertThat(실제주문.getId()).isNotNull();
        }

        @Test
        void 빈_테이블일_경우_예외가_발생한다() {
            //given
            OrderLineItem 주문상품 = 주문_상품_만들기();

            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());
            long 빈_테이블_아이디 = 1L;
            주문.setOrderTableId(빈_테이블_아이디);

            //expect
            assertThatThrownBy(() -> orderService.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않는경우_예외가_발생한다() {
            //given
            OrderLineItem 주문상품 = 주문_상품_만들기();
            OrderTable 삭제된_테이블 = 비어있지_않은_테이블_생성();
            테이블_지우기(삭제된_테이블);

            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());
            주문.setOrderTableId(삭제된_테이블.getId());

            //expect
            assertThatThrownBy(() -> orderService.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        private void 테이블_지우기(OrderTable 테이블) {
            jdbcTemplate.update("delete from order_table where id = ?", 테이블.getId());
        }

        @Test
        void 주문에_포함된_상품이_없으면_예외가_발생한다() {
            //given
            Order 주문 = new Order();
            주문.setOrderLineItems(emptyList());
            주문.setOrderStatus(OrderStatus.COOKING.name());
            OrderTable 테이블 = 비어있지_않은_테이블_생성();
            주문.setOrderTableId(테이블.getId());

            //expect
            assertThatThrownBy(() -> orderService.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 같은_상품이_있으면_예외가_발생한다() {
            //given
            OrderLineItem 주문상품 = 주문_상품_만들기();
            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품, 주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());
            OrderTable 테이블 = 비어있지_않은_테이블_생성();
            주문.setOrderTableId(테이블.getId());

            //expect
            assertThatThrownBy(() -> orderService.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    private OrderTable 비어있지_않은_테이블_생성() {
        final var 테이블 = new OrderTable();
        테이블.setEmpty(false);
        테이블.setNumberOfGuests(4);
        return orderTableDao.save(테이블);
    }

    private OrderLineItem 주문_상품_만들기() {
        final var 메뉴 = menuDao.findAll().get(0);
        OrderLineItem 주문상품 = new OrderLineItem();
        주문상품.setMenuId(메뉴.getId());
        주문상품.setQuantity(1);
        return 주문상품;
    }

    @Test
    void 주문_리스트_조회() {
        //given
        var 존재하는_주문_아이디 = orderDao.findAll().stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        //when
        List<Order> 주문_리스트 = orderService.list();

        //then
        assertThat(주문_리스트).extracting(Order::getId)
                .containsAll(존재하는_주문_아이디);
    }

    @Nested
    class 주문_상태_변경 {

        private Order 주문_생성() {
            OrderLineItem 주문상품 = 주문_상품_만들기();

            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());

            OrderTable 테이블 = 비어있지_않은_테이블_생성();
            주문.setOrderTableId(테이블.getId());

            return orderService.create(주문);
        }

        @Test
        void 성공() {
            //given
            Order 주문 = 주문_생성();
            주문.setOrderStatus(OrderStatus.COOKING.name());

            //when
            Order 실제주문 = orderService.changeOrderStatus(주문.getId(), 주문);

            //then
            assertThat(실제주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @Test
        void 주문이_존재하지_않으면_예외가_발생한다() {
            //expect
            assertThatThrownBy(() -> orderService.changeOrderStatus(1000000L, new Order()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_COMPLETION이면_예외가_발생한다() {
            //given
            Order 주문 = 주문_생성();
            주문.setOrderStatus(OrderStatus.COMPLETION.name());
            orderDao.save(주문);

            //expect
            assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
