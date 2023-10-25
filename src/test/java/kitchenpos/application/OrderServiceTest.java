package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderCreateRequest.OrderLineRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderServiceTest {

    private OrderService orderService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
                menuRepository,
                orderRepository,
                orderLineItemRepository,
                orderTableRepository
        );
    }

    @Test
    void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
        OrderCreateRequest request = new OrderCreateRequest(1L, List.of(new OrderLineRequest(1L, 1)));
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 등록할 수 없는 빈 테이블 입니다.");
    }

    @Test
    void 주문_항목에_메뉴_ID가_존재하지_않는경우_예외가_발생한다() {
        OrderCreateRequest request = new OrderCreateRequest(9L, List.of(new OrderLineRequest(0L, 1)));
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("메뉴가 존재하지 않습니다.");
    }

    @Test
    void 주문_생성할_수_있다() {
        OrderCreateRequest request = new OrderCreateRequest(9L, List.of(new OrderLineRequest(1L, 1)));
        OrderResponse orderResponse = orderService.create(request);
        Assertions.assertThat(orderRepository.findById(orderResponse.getId())).isPresent();
    }

    @Test
    void 전체_주문_조회할_수_있다() {
        OrderCreateRequest request = new OrderCreateRequest(9L, List.of(new OrderLineRequest(1L, 1)));
        orderService.create(request);
        List<OrderResponse> list = orderService.list();
        Assertions.assertThat(list).isNotNull();
    }

    @Test
    void 주문_ID가_존재하지_않는_경우_예외가_발생한다() {
        OrderStatusChangeRequest request = new OrderStatusChangeRequest("COMPLETION");
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("존재하지 않는 주문입니다.");
    }

    @Test
    void 주문_상태가_COMPLETION_상태이면_예외가_발생한다() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(9L, List.of(new OrderLineRequest(1L, 1)));
        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("COMPLETION");
        orderService.changeOrderStatus(orderResponse.getId(), orderStatusChangeRequest);
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), orderStatusChangeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 주문이 완료되었습니다.");
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(9L, List.of(new OrderLineRequest(1L, 1)));
        orderService.create(orderCreateRequest);
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("COMPLETION");
        OrderResponse orderResponse = orderService.changeOrderStatus(1L, orderStatusChangeRequest);
        Assertions.assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
