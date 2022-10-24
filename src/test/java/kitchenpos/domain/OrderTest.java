package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("아이디를 설정한다")
    void setId(){
        // given
        Order order = new Order();
        Long id = 999L;

        // when
        order.setId(id);

        // then
        assertThat(order.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("주문 테이블 아이디를 설정한다")
    void setOrderTableId(){
        // given
        Order order = new Order();
        Long orderTableId = 999L;

        // when
        order.setOrderTableId(orderTableId);

        // then
        assertThat(order.getOrderTableId()).isEqualTo(orderTableId);
    }

    @Test
    @DisplayName("주문 상태를 설정한다")
    void setOrderStatus(){
        // given
        Order order = new Order();
        String orderStatus = "status";

        // when
        order.setOrderStatus(orderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    @Test
    @DisplayName("주문 시간을 설정한다")
    void setOrderedTime(){
        // given
        Order order = new Order();
        LocalDateTime orderedTime = LocalDateTime.now();

        // when
        order.setOrderedTime(orderedTime);

        // then
        assertThat(order.getOrderedTime()).isEqualTo(orderedTime);
    }

    @Test
    @DisplayName("주문 항목들을 설정한다")
    void seOrderLineItems(){
        // given
        Order order = new Order();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();
        List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2);

        // when
        order.setOrderLineItems(orderLineItems);

        // then
        assertAll(
            () -> assertThat(order.getOrderLineItems()).hasSize(2),
            () -> assertThat(order.getOrderLineItems()).containsExactly(orderLineItem1, orderLineItem2)
        );
    }
}
