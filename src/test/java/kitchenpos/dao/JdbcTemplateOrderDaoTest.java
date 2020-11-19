package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@JdbcTest
@Import(JdbcTemplateOrderDao.class)
class JdbcTemplateOrderDaoTest {
    @Autowired
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @DisplayName("OrderDao save 테스트")
    @Test
    void save() {
        // Given
        final Order order = new Order();
        order.setOrderTableId(2L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        // When
        final Order savedOrder = jdbcTemplateOrderDao.save(order);

        // Then
        assertAll(
                () -> assertThat(savedOrder)
                        .extracting(Order::getId)
                        .isNotNull()
                ,
                () -> assertThat(savedOrder)
                        .extracting(Order::getOrderTableId)
                        .isEqualTo(order.getOrderTableId())
                ,
                () -> assertThat(savedOrder)
                        .extracting(Order::getOrderStatus)
                        .isEqualTo(order.getOrderStatus())
                ,
                () -> assertThat(savedOrder)
                        .extracting(Order::getOrderedTime)
                        .isEqualTo(order.getOrderedTime())
        );
    }

    @DisplayName("OrderDao findById 테스트")
    @Test
    void findById() {
        // When
        final Order order = jdbcTemplateOrderDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertAll(
                () -> assertThat(order)
                        .extracting(Order::getOrderTableId)
                        .isEqualTo(1L)
                ,
                () -> assertThat(order)
                        .extracting(Order::getOrderStatus)
                        .isEqualTo(OrderStatus.MEAL.name())
                ,
                () -> assertThat(order)
                        .extracting(Order::getOrderedTime)
                        .isEqualTo(LocalDateTime.of(2020, 11, 18, 12, 0, 0))
        );
    }

    @DisplayName("OrderDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<Order> orders = jdbcTemplateOrderDao.findAll();

        // Then
        assertThat(orders).hasSize(1);
    }

    @DisplayName("OrderDao existsByOrderTableIdAndOrderStatusIn 테스트")
    @ParameterizedTest
    @MethodSource("generateOrderTableIdAndOrderStatuses")
    void existsByOrderTableIdAndOrderStatusIn(
            final long orderTableId,
            final List<String> orderStatuses,
            final boolean expected
    ) {
        // When
        final boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, orderStatuses);

        // Then
        assertThat(exists).isEqualTo(expected);
    }

    @DisplayName("OrderDao existsByOrderTableIdInAndOrderStatusIn 테스트")
    @ParameterizedTest
    @MethodSource("generateOrderTableIdsAndOrderStatuses")
    void existsByOrderTableIdInAndOrderStatusIn(
            final List<Long> orderTableIds,
            final List<String> orderStatuses,
            final boolean expected
    ) {
        // When
        final boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, orderStatuses);

        // Then
        assertThat(exists).isEqualTo(expected);
    }

    private static Stream<Arguments> generateOrderTableIdAndOrderStatuses() {
        return Stream.of(
                Arguments.arguments(1L,
                        newArrayList(OrderStatus.MEAL.name(), OrderStatus.COMPLETION.name()), true)
                ,
                Arguments.arguments(1L,
                        newArrayList(OrderStatus.COOKING.name(), OrderStatus.COMPLETION.name()),
                        false)
        );
    }

    private static Stream<Arguments> generateOrderTableIdsAndOrderStatuses() {
        return Stream.of(
                Arguments.arguments(newArrayList(1L, 2L, 3L),
                        newArrayList(OrderStatus.MEAL.name(), OrderStatus.COMPLETION.name()), true)
                ,
                Arguments.arguments(newArrayList(1L, 2L, 3L),
                        newArrayList(OrderStatus.COOKING.name(), OrderStatus.COMPLETION.name()),
                        false)
        );
    }
}
