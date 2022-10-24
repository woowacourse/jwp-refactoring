package kitchenpos.application;

import static kitchenpos.application.TestFixture.메뉴_그룹_생성;
import static kitchenpos.application.TestFixture.메뉴_상품_생성;
import static kitchenpos.application.TestFixture.메뉴_생성;
import static kitchenpos.application.TestFixture.상품_생성;
import static kitchenpos.application.TestFixture.주문_상품_생성;
import static kitchenpos.application.TestFixture.주문_생성;
import static kitchenpos.application.TestFixture.주문_테이블_생성;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

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
    void 주문을_생성한다() {
        // given
        final OrderTable orderTable = tableService.create(주문_테이블_생성(1, false));
        final Product product = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = menuService.create(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());

        // when
        final Order order = orderService.create(주문_생성(List.of(orderLineItem), orderTable.getId()));

        // then
        assertAll(
                () -> assertThat(order.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(order.getOrderLineItems().get(0).getMenuId()).isEqualTo(menu.getId())
        );
    }

    @Test
    void 주문_생성시_주문_항목이_비어있을_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = tableService.create(주문_테이블_생성(1, false));

        // when, then
        assertThatThrownBy(() -> orderService.create(주문_생성(List.of(), orderTable.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성시_메뉴에_등록된_상품이_아니라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = tableService.create(주문_테이블_생성(1, false));
        final OrderLineItem orderLineItem = 주문_상품_생성(1L);

        // when, then
        assertThatThrownBy(() -> orderService.create(주문_생성(List.of(orderLineItem), orderTable.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성시_주문_테이블에_등록되지_않았다면_예외가_발생한다() {
        // given
        final Product product = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = menuService.create(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());

        // when, then
        assertThatThrownBy(() -> orderService.create(주문_생성(List.of(orderLineItem), 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_전체_조회를_한다() {
        // given
        final OrderTable orderTable = tableService.create(주문_테이블_생성(1, false));
        final Product product = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = menuService.create(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());
        final Order order = orderService.create(주문_생성(List.of(orderLineItem), orderTable.getId()));

        // when
        final List<Order> orders = orderService.list();

        // then
        assertAll(
                () -> assertThat(orders.size()).isOne(),
                () -> assertThat(orders.get(0).getId()).isEqualTo(order.getId())
        );
    }

    @ParameterizedTest
    @EnumSource(names = {"MEAL", "COMPLETION"})
    void 주문_상태를_변경한다(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable = tableService.create(주문_테이블_생성(1, false));
        final Product product = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = menuService.create(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());
        final Long orderId = orderService.create(주문_생성(List.of(orderLineItem), orderTable.getId())).getId();

        // when
        final Order order = orderService.changeOrderStatus(orderId, 주문_생성(orderStatus));

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    @Test
    void 주문_상태_변경시_주문하지_않은_경우_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 주문_생성(OrderStatus.COMPLETION)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경시_현재_주문_상태가_COMPLETION인_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = tableService.create(주문_테이블_생성(1, false));
        final Product product = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = menuService.create(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());
        final Long orderId = orderService.create(주문_생성(List.of(orderLineItem), orderTable.getId())).getId();
        final Order order = orderService.changeOrderStatus(orderId, 주문_생성(OrderStatus.COMPLETION));

        //when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), 주문_생성(OrderStatus.COMPLETION)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
