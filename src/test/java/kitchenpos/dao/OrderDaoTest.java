package kitchenpos.dao;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderDao 테스트")
@SpringBootTest
@Transactional
class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private static Stream<Arguments> saveFailureWhenDbLimit() {
        return Stream.of(
                Arguments.of(0L, OrderStatus.COOKING.name(), LocalDateTime.now()),
                Arguments.of(null, OrderStatus.COOKING.name(), LocalDateTime.now()),
                Arguments.of(OrderTableFixture.FIRST.getId(), null, LocalDateTime.now()),
                Arguments.of(OrderTableFixture.FIRST.getId(), OrderStatus.COOKING.name(), null)
        );
    }

    @DisplayName("주문 저장 - 실패 - DB 제약사항")
    @CustomParameterizedTest
    @MethodSource("saveFailureWhenDbLimit")
    void saveFailureWhenDbLimit(Long orderTableId, String orderStatus, LocalDateTime localDateTime) {
        //given
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(localDateTime);
        //when
        //then
        assertThatThrownBy(() -> orderDao.save(order))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("주문 존재 확인 - 성공 - 주문테이블 id, 주문상태")
    @Test
    void existsByOrderTableIdAndOrderStatus() {
        //given
        final OrderTable ordertable = orderTableDao.save(OrderTableFixture.TABLE_BEFORE_SAVE);
        final Order order = new Order();
        order.setOrderTableId(ordertable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        //when
        final Order expect = orderDao.save(order);
        final boolean actual = orderDao.existsByOrderTableIdAndOrderStatusIn(expect.getOrderTableId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("주문 존재 확인 - 성공 - 주문테이블들의 id, 주문상태")
    @Test
    void existsByOrderTablesIdAndOrderStatus() {
        //given
        final OrderTable ordertable = orderTableDao.save(OrderTableFixture.TABLE_BEFORE_SAVE);
        final Order order = new Order();
        order.setOrderTableId(ordertable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        //when
        final Order firstExpect = orderDao.save(order);
        final Order secondExpect = orderDao.save(order);
        final boolean actual = orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(firstExpect.getOrderTableId(), secondExpect.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        //then
        assertThat(actual).isTrue();
    }
}