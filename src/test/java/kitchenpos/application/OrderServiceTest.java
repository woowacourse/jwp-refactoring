package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidOrderLineItemNonRegisteredException;
import kitchenpos.exception.NotFoundOrderTableException;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationTest {

    @Test
    void 주문을_생성할_수_있다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        Menu saveMenu = menuService.create(createMenu);
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

        // when
        Order extract = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(),
            List.of(new OrderLineItem(saveMenu.getId(), 1))));

        // then
        assertThat(extract).isNotNull();
    }

    @Test
    void 주문할_아이템을_입력하지_않으면_예외가_발생한다()  {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

        // when & then
        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), null)));
    }

    @Test
    void 등록되지_않은_메뉴로_주문을_하면_예외가_발생한다() {
        // given
        menuGroupService.create(new MenuGroup("1인 메뉴"));
        productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Long notRegisterMenuId = 100L;
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

        // when & then
        assertThatThrownBy(
            () -> orderService.create(new Order(orderTable.getId(), LocalDateTime.now(),
                List.of(new OrderLineItem(notRegisterMenuId, 1)))))
            .isInstanceOf(InvalidOrderLineItemNonRegisteredException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        Menu saveMenu = menuService.create(createMenu);
        Long notRegisterOrderTableId = 100L;

        // when & then
        assertThatThrownBy(() -> orderService.create(new Order(notRegisterOrderTableId, LocalDateTime.now(),
            List.of(new OrderLineItem(saveMenu.getId(), 1)))))
            .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 주문한_목록들을_조회할_수_있다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        Menu saveMenu = menuService.create(createMenu);
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
        Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

        // when
        List<Order> extract = orderService.list();

        // then
        assertThat(extract).hasSize(1);
    }

    @Test
    void 주문의_상태를_변경할_수_있다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        Menu saveMenu = menuService.create(createMenu);
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
        Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

        // when
        Order extract = orderService.changeOrderStatus(order.getId(),
            new Order(orderTable.getId(), OrderStatus.MEAL.name()));

        // then
        assertThat(extract.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문이_완료_상태일_때_상태를_변경하면_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        Menu saveMenu = menuService.create(createMenu);
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
        Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));
        orderService.changeOrderStatus(order.getId(), new Order(orderTable.getId(), OrderStatus.COMPLETION.name()));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order(orderTable.getId(), OrderStatus.MEAL.name())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_존재하지_않는_상태로_변경할_경우_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        Menu saveMenu = menuService.create(createMenu);
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
        Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order(orderTable.getId(), "Not Registered OrderStatus")))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
