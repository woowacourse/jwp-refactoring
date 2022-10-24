package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JdbcTemplateOrderDaoTest extends JdbcTemplateTest{

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final Order savedOrder = orderDao.save(getOrder());
        assertThat(savedOrder.getId()).isNotNull();
    }

    @ParameterizedTest(name = "해당 주문의 주문테이블 상태 {2} 이(가) {3} 에 속하면 true 를 반환한다.")
    @MethodSource("invalidParams")
    void existsByOrderTableIdInAndOrderStatusIn(final Order order,
                                                final List<String> orderStatuses,
                                                final String testOrderStatus,
                                                final String testOrderStatuses) {
        // given
        orderDao.save(order);

        // when
        final boolean actual = orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(order.getOrderTableId()),
                orderStatuses);

        // then
        assertThat(actual).isTrue();
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(new Order(1L, COOKING.name(), LocalDateTime.now()),
                        Arrays.asList(COOKING.name(), MEAL.name()),
                        COOKING.name(),
                        "COOKING, MEAL"),
                Arguments.of(new Order(1L, MEAL.name(), LocalDateTime.now()),
                        Arrays.asList(COOKING.name(), MEAL.name()),
                        MEAL.name(),
                        "COOKING, MEAL")
        );
    }
}
