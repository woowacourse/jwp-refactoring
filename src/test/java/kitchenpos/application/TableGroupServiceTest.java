package kitchenpos.application;

import static kitchenpos.application.TestFixture.메뉴_그룹_생성;
import static kitchenpos.application.TestFixture.메뉴_상품_생성;
import static kitchenpos.application.TestFixture.메뉴_생성;
import static kitchenpos.application.TestFixture.상품_생성;
import static kitchenpos.application.TestFixture.주문_상품_생성;
import static kitchenpos.application.TestFixture.주문_생성;
import static kitchenpos.application.TestFixture.주문_테이블_생성;
import static kitchenpos.application.TestFixture.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        final OrderTable orderTable1 = tableService.create(주문_테이블_생성(1, true));
        final OrderTable orderTable2 = tableService.create(주문_테이블_생성(1, true));

        // when
        final TableGroup tableGroup = tableGroupService.create(
                테이블_그룹_생성(List.of(orderTable1, orderTable2))
        );

        // then
        assertThat(tableGroup.getOrderTables()).usingElementComparatorOnFields("id")
                .containsExactly(orderTable1, orderTable2);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_3개_미만_인_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = tableService.create(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(List.of(orderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_등록되지_않은_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블_생성(1, true);
        final OrderTable orderTable2 = tableService.create(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_비어있지_않은_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블_생성(1, false);
        final OrderTable orderTable2 = tableService.create(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_이미_테이블_그룹을_가진_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = TestFixture.주문_테이블_생성(1L, 1, true);
        final OrderTable orderTable2 = tableService.create(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final OrderTable orderTable1 = tableService.create(주문_테이블_생성(1, true));
        final OrderTable orderTable2 = tableService.create(주문_테이블_생성(1, true));
        final TableGroup tableGroup = tableGroupService.create(
                테이블_그룹_생성(List.of(orderTable1, orderTable2))
        );

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> orderTables = tableService.list();
        assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(1).getTableGroupId()).isNull()
        );
    }

    @Test
    void 테이블_그룹_해제시_주문_테이블에_등록되어_있고_주문_상태가_COOKING_인_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = tableService.create(주문_테이블_생성(1, true));
        final OrderTable orderTable2 = tableService.create(주문_테이블_생성(1, true));
        final TableGroup tableGroup = tableGroupService.create(
                테이블_그룹_생성(List.of(orderTable1, orderTable2))
        );
        final Product product = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = menuService.create(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());
        orderService.create(주문_생성(List.of(orderLineItem), orderTable1.getId()));
        orderService.create(주문_생성(List.of(orderLineItem), orderTable2.getId()));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_해제시_주문_테이블에_등록되어_있고_주문_상태가_MEAL_인_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = tableService.create(주문_테이블_생성(1, true));
        final OrderTable orderTable2 = tableService.create(주문_테이블_생성(1, true));
        final TableGroup tableGroup = tableGroupService.create(
                테이블_그룹_생성(List.of(orderTable1, orderTable2))
        );
        final Product product = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = menuService.create(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());
        final Order order1 = orderService.create(주문_생성(List.of(orderLineItem), orderTable1.getId()));
        final Order order2 = orderService.create(주문_생성(List.of(orderLineItem), orderTable2.getId()));

        orderService.changeOrderStatus(order1.getId(), 주문_생성(OrderStatus.MEAL));
        orderService.changeOrderStatus(order2.getId(), 주문_생성(OrderStatus.MEAL));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
