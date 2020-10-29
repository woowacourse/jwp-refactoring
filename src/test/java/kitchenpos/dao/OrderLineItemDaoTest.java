package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        assertAll(
            () -> assertThat(orderLineItems).hasSize(2),
            () -> assertThat(orderLineItems.get(0)).usingRecursiveComparison().isEqualTo(ORDER_LINE_ITEM_1),
            () -> assertThat(orderLineItems.get(1)).usingRecursiveComparison().isEqualTo(ORDER_LINE_ITEM_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 주문메뉴가 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<OrderLineItem> orderLineItem = orderLineItemDao.findById(-1L);

        assertThat(orderLineItem).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        OrderLineItem orderLineItem = orderLineItemDao.findById(ORDER_LINE_ITEM_SEQ_1).get();

        assertThat(orderLineItem).usingRecursiveComparison().isEqualTo(ORDER_LINE_ITEM_1);
    }

    @DisplayName("주문id로 전체조회 테스트")
    @Test
    void findAllByOrderIdTest() {
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(ORDER_ID_1);

        assertAll(
            () -> assertThat(orderLineItems).hasSize(1),
            () -> assertThat(orderLineItems.get(0)).usingRecursiveComparison().isEqualTo(ORDER_LINE_ITEM_1)
        );
    }
}