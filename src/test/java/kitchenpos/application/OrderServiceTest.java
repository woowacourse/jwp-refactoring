package kitchenpos.application;

import static kitchenpos.Fixture.주문정보;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.OrderResponse;
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
        Order request = new Order(테이블_생성(false).getId(), null, List.of(주문정보(메뉴_생성().getId())));

        OrderResponse actual = orderService.create(request);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 주문을_생성할때_주문정보와_저장된_메뉴와_다를_경우_예외를_발생시킨다() {
        Order request = new Order(테이블_생성(false).getId(), null, List.of(주문정보(-1L)));

        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문정보가 존재하지 않습니다.");
    }

    @Test
    void 주문을_생성할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        Order request = new Order(-1L, null, List.of(주문정보(메뉴_생성().getId())));

        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 생성하기 위한 테이블이 존재하지 않습니다.");
    }

    @Test
    void 테이블이_빈_경우_예외를_발생시킨다() {
        Order request = new Order(테이블_생성(true).getId(), null, List.of(주문정보(메뉴_생성().getId())));

        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 비었습니다");
    }

    @Test
    void 주문을_조회한다() {
        주문_생성();

        List<OrderResponse> orders = orderService.list();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 주문의_상태를_조리에서_식사로_바꾼다() {
        Order savedOrder = 주문_생성();

        OrderResponse result = orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문의_상태를_바꿀때_주문이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, OrderStatus.MEAL))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("상태를 변화시키기 위한 주문이 없습니다.");
    }

    @Test
    void 주문의_상태를_바꿀때_주문의_상태가_계산완료인_경우_예외를_발생시킨다() {
        Order savedOrder = 주문_생성();

        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 완료된 상태이므로 상태를 변화시킬 수 없습니다.");
    }
}
