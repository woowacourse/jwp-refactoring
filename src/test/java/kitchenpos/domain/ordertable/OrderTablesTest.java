package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTablesTest {

    @Test
    void of_메서드는_유효한_데이터를_전달하면_orderTables를_초기화한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order1 = new Order(orderTable1, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        final Order order2 = new Order(orderTable2, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        orderTable1.initOrder(order1);
        orderTable2.initOrder(order2);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when
        final OrderTables actual = OrderTables.of(tableGroup, List.of(orderTable1, orderTable2));

        // then
        assertAll(
                () -> assertThat(actual.getOrderTables()).hasSize(2),
                () -> assertThat(actual.getOrderTables().get(0).getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(actual.getOrderTables().get(1).getTableGroup()).isEqualTo(tableGroup)
        );
    }

    @Test
    void of_메서드는_orderTables가_비어_있다면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order1 = new Order(orderTable1, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        final Order order2 = new Order(orderTable2, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        orderTable1.initOrder(order1);
        orderTable2.initOrder(order2);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> OrderTables.of(tableGroup, Collections.emptyList()))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @Test
    void of_메서드는_orderTables의_크기가_2_미만이라면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order1 = new Order(orderTable1, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        final Order order2 = new Order(orderTable2, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        orderTable1.initOrder(order1);
        orderTable2.initOrder(order2);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> OrderTables.of(tableGroup, List.of(orderTable1)))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @Test
    void ungroup_메서드는_호출하면_모든_orderTable과_tableGroup간의_관계를_끊는다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
        final Order order1 = new Order(orderTable1, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        final Order order2 = new Order(orderTable2, orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        orderTable1.initOrder(order1);
        orderTable2.initOrder(order2);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
        final OrderTables orderTables = OrderTables.of(tableGroup, List.of(orderTable1, orderTable2));

        // when
        orderTables.ungroup();

        // then
        assertAll(
                () -> assertThat(orderTables.getOrderTables()).hasSize(2),
                () -> assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isNull(),
                () -> assertThat(orderTables.getOrderTables().get(0).isEmpty()).isTrue(),
                () -> assertThat(orderTables.getOrderTables().get(1).getTableGroup()).isNull(),
                () -> assertThat(orderTables.getOrderTables().get(1).isEmpty()).isTrue()
        );
    }
}
