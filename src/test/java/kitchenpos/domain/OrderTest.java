package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @Test
    void invalidOrderTable() {
        Product chicken = new Product("치킨", 18000);
        Product pizza = new Product("피자", 21000);
        MenuProduct menuChicken = new MenuProduct(1L, chicken, 1);
        MenuProduct menuPizza = new MenuProduct(1L, pizza, 1);
        Menu menu1 = new Menu("1치킨 1피자", 39000, null, List.of(menuChicken, menuPizza));
        Product engawa = new Product("광어 지느러미 초밥", 17000);
        Product salmon = new Product("연어 초밥", 15000);
        MenuProduct menuEngawa = new MenuProduct(1L, engawa, 1);
        MenuProduct menuSalmon = new MenuProduct(1L, salmon, 1);
        Menu menu2 = new Menu("광어연어 초밥", 32000, null, List.of(menuEngawa, menuSalmon));
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, menu1, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, menu2, 1);

        assertThatThrownBy(() -> new Order(null, List.of(orderLineItem1, orderLineItem2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 없습니다.");
    }

    @Test
    void invalidOrderLineItem() {
        Product chicken = new Product("치킨", 18000);
        Product pizza = new Product("피자", 21000);
        MenuProduct menuChicken = new MenuProduct(1L, chicken, 1);
        MenuProduct menuPizza = new MenuProduct(1L, pizza, 1);
        Menu menu1 = new Menu("1치킨 1피자", 39000, null, List.of(menuChicken, menuPizza));
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, menu1, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, null, 1);
        OrderTable orderTable = new OrderTable(4);

        assertThatThrownBy(() -> new Order(orderTable, List.of(orderLineItem1, orderLineItem2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목의 메뉴는 유효한 메뉴여야 합니다.");
    }

    @Test
    void EmptyOrderLineItem() {
        OrderTable orderTable = new OrderTable(4);

        assertThatThrownBy(() -> new Order(orderTable, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 최소 1개 이상 필요합니다.");
    }
}
