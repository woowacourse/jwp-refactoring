package kitchenpos.application;

import static kitchenpos.Fixture.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_생성한다() {
        Order order = 주문(테이블_생성(false).getId(), 메뉴_생성().getId());

        Order actual = orderService.create(order);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 주문을_생성할때_주문정보가_없는_경우_예외를_발생시킨다() {
        Order emptyOrderItem = new Order(테이블_생성(false).getId(), null);

        assertThatThrownBy(() -> orderService.create(emptyOrderItem))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_주문정보와_저장된_메뉴와_다를_경우_예외를_발생시킨다() {
        Order order = 주문(테이블_생성(false).getId(), -1L);

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        Order order = 주문(-1L, 메뉴_생성().getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_빈_경우_예외를_발생시킨다() {
        Order order = 주문(테이블_생성(true).getId(), 메뉴_생성().getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회한다() {
        주문_생성();

        List<Order> orders = orderService.list();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 주문의_상태를_조리에서_식사로_바꾼다() {
        Order savedOrder = 주문_생성();

        savedOrder.setOrderStatus("MEAL");
        Order result = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문의_상태를_바꿀때_주문이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_바꿀때_주문의_상태가_계산완료인_경우_예외를_발생시킨다() {
        Order savedOrder = 주문_생성();

        savedOrder.setOrderStatus("COMPLETION");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), savedOrder))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
