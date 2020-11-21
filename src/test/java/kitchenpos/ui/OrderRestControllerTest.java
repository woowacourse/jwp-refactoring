package kitchenpos.ui;

import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderRestControllerTest extends ControllerTest {

    private OrderLineItem chickenOrderItem;
    private OrderLineItem pizzaOrderItem;
    private OrderTable table;

    @BeforeEach
    void setUp() throws Exception {
        // given
        MenuGroup chickenCombo = menuGroup("Chicken Combo");
        MenuProduct friedChicken = menuProduct(product("Fried Chicken", 14000L), 1);
        MenuProduct coke = menuProduct(product("Coke", 1000L), 1);
        Menu friedChickenCombo = menu("Fried Chicken Combo", 14500L, chickenCombo, Arrays.asList(friedChicken, coke));
        chickenOrderItem = orderLineItem(friedChickenCombo, 1);
        MenuGroup pizzaCombo = menuGroup("Pizza Combo");
        MenuProduct cheesePizza = menuProduct(product("Cheese Chicken", 13000L), 1);
        MenuProduct zeroCoke = menuProduct(product("Zero Coke", 1000L), 1);
        Menu cheesePizzaCombo = menu("Cheese Pizza Combo", 13500L, pizzaCombo, Arrays.asList(cheesePizza, zeroCoke));
        pizzaOrderItem = orderLineItem(cheesePizzaCombo, 1);
        table = table();
    }

    @Test
    void create() throws Exception {
        // when
        Order order = order(Arrays.asList(chickenOrderItem, pizzaOrderItem), table);

        // then
        assertAll(
                () -> assertThat(order.getId()).isEqualTo(1L),
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderTableId()).isEqualTo(table.getId()),
                () -> assertThat(order.getOrderLineItems()).isNotEmpty(),
                () -> assertThat(order.getOrderLineItems().get(0).getOrderId()).isEqualTo(order.getId())
        );
    }

    @Test
    void create_emptyItems() {
        // when
        assertThatThrownBy(() -> order(Collections.EMPTY_LIST, table))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_nonExistingItem() {
        // given
        OrderLineItem nonExistingItem = new OrderLineItem();
        nonExistingItem.setMenuId(5L);

        // when
        assertThatThrownBy(() -> order(Arrays.asList(chickenOrderItem, nonExistingItem), table))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_nonExistingTable() {
        // given
        OrderTable nonExistingTable = new OrderTable();
        nonExistingTable.setId(2L);

        // when
        assertThatThrownBy(() -> order(Arrays.asList(chickenOrderItem, pizzaOrderItem), nonExistingTable))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_emptyTable() throws Exception {
        // given
        OrderTable emptyTable = table();
        emptyTable.setEmpty(true);
        changeEmpty(emptyTable);

        // when
        assertThatThrownBy(() -> order(Arrays.asList(chickenOrderItem, pizzaOrderItem), emptyTable))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() throws Exception {
        // given
        Order order = order(Arrays.asList(chickenOrderItem, pizzaOrderItem), table);
        Order order2 = order(Arrays.asList(chickenOrderItem, pizzaOrderItem), table);

        // when
        List<Order> orders = orders();

        // then
        assertAll(
                () -> assertThat(orders).hasSize(2),
                () -> assertThat(orders.get(0)).usingRecursiveComparison().isEqualTo(order),
                () -> assertThat(orders.get(1)).usingRecursiveComparison().isEqualTo(order2)
        );
    }

    @Test
    void changeOrderStatus() throws Exception {
        // given
        Order order = order(Arrays.asList(chickenOrderItem, pizzaOrderItem), table);

        // when
        order.setOrderStatus(OrderStatus.MEAL.name());
        Order changedOrder = changeStatus(order);

        // then
        assertAll(
                () -> assertThat(changedOrder.getId()).isEqualTo(order.getId()),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }

    @Test
    void changeOrderStatus_nonExisting() {
        // given
        Order nonExistingOrder = new Order();
        nonExistingOrder.setId(2L);
        nonExistingOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when
        assertThatThrownBy(() -> changeStatus(nonExistingOrder))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatus_alreadyComplete() throws Exception {
        // given
        Order order = order(Arrays.asList(chickenOrderItem, pizzaOrderItem), table);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        changeStatus(order);
        order.setOrderStatus(OrderStatus.MEAL.name());
        assertThatThrownBy(() -> changeStatus(order))
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}