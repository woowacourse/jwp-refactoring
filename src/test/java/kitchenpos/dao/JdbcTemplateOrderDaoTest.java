package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateOrderDaoTest {

    private final OrderDao orderDao;

    @Autowired
    JdbcTemplateOrderDaoTest(final DataSource dataSource) {
        this.orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @Test
    void 저장한다() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        // when
        Order savedOrder = orderDao.save(order);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(1L)
        );
    }

    @Test
    void 이미_ID가_존재하면_update를_진행한다() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        // when
        savedOrder.setOrderStatus(COMPLETION.name());
        Order updatedOrder = orderDao.save(savedOrder);

        // then
        Assertions.assertAll(
                () -> assertThat(updatedOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo("COMPLETION")
        );
    }

    @Test
    void ID로_order를_조회한다() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        // when
        Optional<Order> foundOrder = orderDao.findById(savedOrder.getId());

        // then
        Assertions.assertAll(

                () -> assertThat(foundOrder).isPresent(),
                () -> assertThat(foundOrder.get())
                        .usingRecursiveComparison()
                        .ignoringFields("orderedTime")
                        .isEqualTo(
                                new Order(
                                        savedOrder.getId(),
                                        savedOrder.getOrderTableId(),
                                        savedOrder.getOrderStatus(),
                                        savedOrder.getOrderedTime(),
                                        savedOrder.getOrderLineItems()
                                )
                        )
        );
    }

    @Test
    void 일치하는_ID가_없을_경우_empty를_반환한다() {
        // given
        long notExistId = 101L;

        // when
        Optional<Order> order = orderDao.findById(notExistId);

        // then
        assertThat(order).isEmpty();
    }

    @Test
    void order_목록을_조회한다() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        // when
        List<Order> orders = orderDao.findAll();

        // then
        assertThat(orders).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("orderedTime")
                .isEqualTo(
                        Arrays.asList(new Order(savedOrder.getId(),
                                order.getOrderTableId(),
                                order.getOrderStatus(),
                                order.getOrderedTime(),
                                order.getOrderLineItems()
                        ))
                );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "COOKING, COMPLETION, true",
            "COOKING, MEAL, true",
            "COMPLETION, COMPLETION, false"
    })
    void order_status들과_order_table_id에_맞는_order가_있는지_확인한다(String orderStatus1, String orderStatus2, boolean expected) {
        // given
        Order order1 = new Order();
        order1.setOrderTableId(1L);
        order1.setOrderStatus(orderStatus1);
        order1.setOrderedTime(LocalDateTime.now());
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setOrderTableId(1L);
        order2.setOrderStatus(orderStatus2);
        order2.setOrderedTime(LocalDateTime.now());
        orderDao.save(order2);

        // when
        boolean actual = orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList("COOKING", "MEAL"));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, COOKING, 2, COMPLETION, true",
            "3, COOKING, 2, COMPLETION, false",
            "1, COOKING, 2, MEAL, true",
            "3, COOKING, 4, MEAL, false",
            "1, COMPLETION, 2, COMPLETION, false"
    })
    void order_status들과_order_table_id들에_맞는_order가_있는지_확인한다(long orderTableId1, String orderStatus1, long orderTableId2,
                                                            String orderStatus2, boolean expected) {
        // given
        Order order1 = new Order();
        order1.setOrderTableId(orderTableId1);
        order1.setOrderStatus(orderStatus1);
        order1.setOrderedTime(LocalDateTime.now());
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setOrderTableId(orderTableId2);
        order2.setOrderStatus(orderStatus2);
        order2.setOrderedTime(LocalDateTime.now());
        orderDao.save(order2);

        // when
        boolean actual = orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList("COOKING", "MEAL"));

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
