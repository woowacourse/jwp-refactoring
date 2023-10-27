package kitchenpos.order.domain;

import kitchenpos.RepositoryTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class OrderRepositoryTest extends RepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문을_조회할_때_주문_아이템과_함께_조회횐다() {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        orderTableRepository.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem(1L, "item", BigDecimal.TEN);
        OrderLineItem orderLineItem_2 = new OrderLineItem(2L, "item2", BigDecimal.TEN);
        Order order = new Order(orderTable.getId(), List.of(orderLineItem, orderLineItem_2), LocalDateTime.now());
        orderRepository.save(order);
        em.flush();
        em.clear();

        // when
        List<Order> orders = orderRepository.findAllWithOrderLineItems();
        for (Order findOrder : orders) {
            em.detach(findOrder);
        }

        // then
        for (Order findOrder : orders) {
            Assertions.assertThatNoException()
                    .isThrownBy(() -> findOrder.getOrderLineItems().get(0).getSeq());
        }
    }
}
