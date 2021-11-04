package kitchenpos.application.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceListTest extends OrderServiceTest {

    @DisplayName("주문들을 조회한다.")
    @Test
    void getOrders() {
        //given
        given(orderDao.findAll()).willReturn(standardOrders);
        given(orderLineItemDao.findAllByOrderId(standardOrder.getId()))
            .willReturn(standardOrderLineItems);

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders.size()).isEqualTo(BASIC_SIZE);
    }

}
