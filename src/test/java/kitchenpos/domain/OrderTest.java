package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class OrderTest {

    private final OrderTable orderTable = new OrderTable(10, true);

    private final Menu menu = new Menu(
        "menu",
        new BigDecimal(1000),
        new MenuGroup("menuGroup"),
        new HashMap<Product, Long>() {{
            put(new Product("product", new BigDecimal(1000)), 1L);
        }}
    );

    Map<Menu, Long> orderLineItems = new HashMap<Menu, Long>() {{
        put(menu, 1L);
    }};

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
    void order() {
        assertDoesNotThrow(() -> new Order(orderTable, orderLineItems));
    }

    @Test
    @DisplayName("주문 테이블이 비어있는 경우 예외가 발생한다.")
    void nullOrderTableId() {
        assertThatThrownBy(() -> new Order(null, orderLineItems))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문 테이블은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("orderLineItems가 비어있는 경우 예외가 발생한다.")
    void emptyOrderItemLines(Map<Menu, Long> items) {
        assertThatThrownBy(() -> new Order(orderTable, items))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문 항목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("COMPLETION 상태인 order를 변경하려는 경우 예외가 발생한다.")
    void completionStatus() {
        // given
        Order order = new Order(orderTable, orderLineItems);
        order.changeStatus(OrderStatus.COMPLETION.name());

        // when, then
        assertThatThrownBy(() -> order.changeStatus("COOKING"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이미 완료된 주문 상태를 변경할 수 없습니다.");
    }

}
