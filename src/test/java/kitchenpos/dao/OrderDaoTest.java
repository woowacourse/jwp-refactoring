package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

class OrderDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<Order> orders = orderDao.findAll();

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
        Optional<Order> order = orderDao.findById(-1L);

        assertThat(order).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        Order order = orderDao.findById(ORDER_ID_1).get();

        assertAll(
            () -> assertThat(order.getId()).isEqualTo(ORDER_ID_1),
            () -> assertThat(order.getOrderedTime()).isEqualTo(ORDERED_TIME_1)
        );
    }

    @DisplayName("테이블 id로 검색 테스트")
    @Test
    void findByTableIds() {
        List<Order> byTableIds = orderDao.findByTableIds(Arrays.asList(TABLE_ID_1, TABLE_ID_2));

        for (Order byTableId : byTableIds) {
            System.out.println(byTableId.getId());
        }
    }

    @DisplayName("해당 테이블에 해당 상태들의 주문이 있는지 조회 테스트")
    @Test
    void existsByTableIdAndOrderStatusInTest() {
        boolean isExist = orderDao
            .existsByTableIdAndOrderStatusIn(TABLE_ID_1, Arrays.asList(ORDER_STATUS_1));

        assertThat(isExist).isTrue();
    }

    @DisplayName("기존에 존재하는 주문 업데이트 테스트")
    @Test
    void updateTest() {
        Order updatingOrder = new Order(ORDER_ID_1, TABLE_ID_1, OrderStatus.COMPLETION, ORDERED_TIME_1);

        orderDao.save(updatingOrder);

        Order updatedOrder = orderDao.findById(ORDER_ID_1).get();
        assertAll(
            () -> assertThat(updatedOrder.getId()).isEqualTo(ORDER_ID_1),
            () -> assertThat(updatedOrder.getOrderedTime()).isEqualTo(ORDERED_TIME_1),
            () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION),
            () -> assertThat(updatedOrder.getTableId()).isEqualTo(TABLE_ID_1)
        );
    }
}