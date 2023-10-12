package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

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

        private OrderTable 비어있지_않은_테이블_생성() {
            final var 테이블 = new OrderTable();
            테이블.setEmpty(false);
            테이블.setNumberOfGuests(4);
            return orderTableDao.save(테이블);
        }

        @Test
        void 빈_테이블일_경우_예외가_발생한다() {
            //given
            OrderLineItem 주문상품 = 주문_상품_만들기();

            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());
            주문.setOrderTableId(1L);

            //expect
            assertThatThrownBy(() -> orderService.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);
        }
        @Test
        void 테이블이_존재하지_않는경우_예외가_발생한다() {
            //given
            OrderLineItem 주문상품 = 주문_상품_만들기();

            Order 주문 = new Order();
            주문.setOrderLineItems(List.of(주문상품));
            주문.setOrderStatus(OrderStatus.COOKING.name());
            주문.setOrderTableId(100000000L);

            //expect
            assertThatThrownBy(() -> orderService.create(주문))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        private OrderLineItem 주문_상품_만들기() {
            final var 메뉴 = menuDao.findAll().get(0);
            OrderLineItem 주문상품 = new OrderLineItem();
            주문상품.setMenuId(메뉴.getId());
            주문상품.setQuantity(1);
            return 주문상품;
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
}
