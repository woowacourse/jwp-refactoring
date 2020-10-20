package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateOrderDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<Order> orders = jdbcTemplateOrderDao.findAll();

        assertAll(
            () -> assertThat(orders).hasSize(2),
            () -> assertThat(orders.get(0).getId()).isEqualTo(ORDER_ID_1),
            () -> assertThat(orders.get(0).getOrderedTime()).isEqualTo(ORDERED_TIME_1),
            () -> assertThat(orders.get(1).getId()).isEqualTo(ORDER_ID_2),
            () -> assertThat(orders.get(1).getOrderedTime()).isEqualTo(ORDERED_TIME_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 주문이 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<Order> order = jdbcTemplateOrderDao.findById(-1L);

        assertThat(order).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        Order order = jdbcTemplateOrderDao.findById(ORDER_ID_1).get();

        assertAll(
            () -> assertThat(order.getId()).isEqualTo(ORDER_ID_1),
            () -> assertThat(order.getOrderedTime()).isEqualTo(ORDERED_TIME_1)
        );
    }

    @DisplayName("해당 테이블에 해당 상태들의 주문이 있는지 조회 테스트")
    @Test
    void existsByOrderTableIdAndOrderStatusInTest() {
        boolean isExist = jdbcTemplateOrderDao
            .existsByOrderTableIdAndOrderStatusIn(ORDER_TABLE_ID_1, Arrays.asList(ORDER_STATUS_1));

        assertThat(isExist).isTrue();
    }

    @DisplayName("해당 테이블들에 해당 상태들의 주문이 있는지 조회 테스트")
    @Test
    void existsByOrderTableIdInAndOrderStatusInTest() {
        boolean isExist = jdbcTemplateOrderDao
            .existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(ORDER_TABLE_ID_1), Arrays.asList(ORDER_STATUS_1));

        assertThat(isExist).isTrue();
    }

    @DisplayName("기존에 존재하는 주문 업데이트 테스트")
    @Test
    void updateTest() {
        Order updatingOrder = new Order();
        updatingOrder.setId(ORDER_ID_1);
        updatingOrder.setOrderedTime(ORDERED_TIME_1);
        updatingOrder.setOrderStatus("COMPLETION");
        updatingOrder.setOrderTableId(ORDER_TABLE_ID_1);

        jdbcTemplateOrderDao.save(updatingOrder);

        Order updatedOrder = jdbcTemplateOrderDao.findById(ORDER_ID_1).get();
        assertAll(
            () -> assertThat(updatedOrder.getId()).isEqualTo(ORDER_ID_1),
            () -> assertThat(updatedOrder.getOrderedTime()).isEqualTo(ORDERED_TIME_1),
            () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo("COMPLETION"),
            () -> assertThat(updatedOrder.getOrderTableId()).isEqualTo(ORDER_TABLE_ID_1)
        );
    }
}