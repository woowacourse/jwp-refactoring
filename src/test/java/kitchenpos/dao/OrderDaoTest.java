package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("OrderDao 는 ")
@SpringTestWithData
class OrderDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블 id로 주문을 찾는다.")
    @Test
    void findByTableId() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final Order savedOrder = orderDao.save(new Order(savedOrderTable.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem())));

        final Order actual = orderDao.findByTableId(savedOrder.getOrderTableId())
                .get();

        assertThat(actual.getId()).isEqualTo(savedOrder.getId());
    }
}
