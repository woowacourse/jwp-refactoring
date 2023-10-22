package kitchenpos.order.application;

import kitchenpos.order.Order;
import kitchenpos.order.application.request.OrderLineItemDto;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.orderlineitem.application.OrderLineItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
class OrderServiceSpringBootTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private EntityManager em;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        //given
        final List<OrderLineItemDto> orderLineItems = List.of(
                new OrderLineItemDto(1L, 1L),
                new OrderLineItemDto(2L, 2L)
        );
        final OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);

        //when
        final Order order = orderService.create(orderRequest);
        em.flush();
        em.clear();

        //then
        assertSoftly(softly -> {
            assertThat(orderRepository.findById(order.getId())).isNotEmpty();
            assertThat(orderLineItemRepository.findAllByOrderId(order.getId())).hasSize(2);
        });
    }
}
