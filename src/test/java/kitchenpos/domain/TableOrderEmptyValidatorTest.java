package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.dao.OrderDao;

@ServiceTest
class TableOrderEmptyValidatorTest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableOrderEmptyValidator tableOrderEmptyValidator;

    @DisplayName("테이블을 비우거나 채울 때 테이블에 완료되지 않은 주문이 있는 경우 예외 처리한다.")
    @Test
    void validate() {
        OrderTable table = tableRepository.save(new OrderTable(1, true));

        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(table.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());

        orderDao.save(order);

        assertThatThrownBy(() -> tableOrderEmptyValidator.validate(table.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}