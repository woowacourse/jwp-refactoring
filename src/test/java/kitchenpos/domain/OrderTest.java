package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    List<OrderLineItem> orderLineItemList;

    @BeforeEach
    void setUp() {
        orderLineItemList = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup("찌개류");
        Menu menu = new Menu("김치찌개세트", BigDecimal.valueOf(1000L), menuGroup, List.of(
                new MenuProduct(new Product("김치찌개", BigDecimal.valueOf(1000L)), 1),
                new MenuProduct(new Product("서비스 공기밥", BigDecimal.valueOf(0L)), 1)
        ));
        orderLineItemList.add(new OrderLineItem(menu, 1));
    }

    @DisplayName("주문 상품이 없으면 예외가 발생한다.")
    @Test
    void orderLineItemIsEmpty() {
        OrderTable orderTable = new OrderTable(1, false);
        assertThatThrownBy(() -> new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void orderTableIsEmpty() {
        OrderTable orderTable = new OrderTable(1, true);
        assertThatThrownBy(() -> new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItemList))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 생성되었다면 주문테이블에 주문이 등록된다.")
    @Test
    void createOrder() {
        OrderTable orderTable = new OrderTable(1, false);
        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItemList);

        assertThat(orderTable.getOrder()).isSameAs(order);
    }
}
