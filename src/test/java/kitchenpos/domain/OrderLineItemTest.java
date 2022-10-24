package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {
    
    @Test
    @DisplayName("순서를 설정한다")
    void setSequence(){
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        Long sequence = 3L;

        // when
        orderLineItem.setSeq(sequence);

        // then
        assertThat(orderLineItem.getSeq()).isEqualTo(sequence);
    }

    @Test
    @DisplayName("주문 아이디를 설정한다")
    void setOrderId(){
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        Long orderId = 999L;

        // when
        orderLineItem.setOrderId(orderId);

        // then
        assertThat(orderLineItem.getOrderId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("Menu 아이디를 설정한다")
    void setMenuId(){
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        Long menuId = 999L;

        // when
        orderLineItem.setMenuId(menuId);

        // then
        assertThat(orderLineItem.getMenuId()).isEqualTo(menuId);
    }

    @Test
    @DisplayName("수량을 설정한다")
    void setQuantity(){
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        long quantity = 3L;

        // when
        orderLineItem.setQuantity(quantity);

        // then
        assertThat(orderLineItem.getQuantity()).isEqualTo(quantity);
    }
}
