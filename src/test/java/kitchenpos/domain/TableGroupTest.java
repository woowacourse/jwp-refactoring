package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    /*
    final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);
     */

    @Test
    void 생성자는_유효한_데이터를_전달하면_tableGroup를_초기화한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, true);

        // when
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // then
        assertAll(
                () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(tableGroup.getOrderTables().get(0).getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(tableGroup.getOrderTables().get(1).getTableGroup()).isEqualTo(tableGroup)
        );
    }

    @ParameterizedTest(name = "orderTables가 {0}이면 예외가 발생한다.")
    @NullAndEmptySource
    void 생성자는_orderTables가_비어_있다면_예외가_발생한다(final List<OrderTable> invalidOrderTables) {
        // when & then
        assertThatThrownBy(() -> new TableGroup(invalidOrderTables))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @Test
    void 생성자는_비어_있지_않은_orderTable을_전달하면_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = new OrderTable(0, true);
        final OrderTable orderTable2 = new OrderTable(0, false);

        // when & then
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable1, orderTable2)))
                .isInstanceOf(InvalidEmptyOrderTableException.class);
    }

    @Test
    void ungroupOrderTables_메서드는_호출하면_tableGroup과_orderTables의_연관관계를_해제한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1);
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
        tableGroup.ungroupOrderTables();

        // then
        assertAll(
                () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(tableGroup.getOrderTables().get(0).getTableGroup()).isNull()
        );
    }
}
