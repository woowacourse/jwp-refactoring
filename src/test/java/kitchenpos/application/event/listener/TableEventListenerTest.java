package kitchenpos.application.event.listener;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.service.ValidateOrderStatusEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TableEventListenerTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    @DisplayName("주문 테이블의 주문 상태가 '조리' 또는 '식사 중'일 경우 예외가 발생한다")
    void cookingOrMealThrowException(OrderStatus orderStatus) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, null, 4));
        orderDao.save(createOrder(null, orderStatus, orderTable.getId(), LocalDateTime.now()));

        assertThatThrownBy(() -> applicationEventPublisher.publishEvent(new ValidateOrderStatusEvent(orderTable)))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 테이블의 주문 상태가 '완료'일 경우 예외가 발생하지 않는다")
    void completionIsValid() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, null, 4));
        orderDao.save(createOrder(null, OrderStatus.COMPLETION, orderTable.getId(), LocalDateTime.now()));

        applicationEventPublisher.publishEvent(new ValidateOrderStatusEvent(orderTable));
    }
}