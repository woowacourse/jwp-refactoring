package kitchenpos.order.service;

import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderCreateServiceTest {

    @Autowired
    private OrderCreateService orderCreateService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문_테이블이_비어있으면_주문할_수_없다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, true, false));
        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(1L, 1)
        );
        Order order = new Order(orderLineItems);
        // when & then
        assertThatThrownBy(() -> orderCreateService.create(orderTable.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
