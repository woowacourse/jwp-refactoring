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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationTest {

    @Nested
    class 주문_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final Menu saveMenu = menuService.create(createMenu);
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when
            final Order extract = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(),
                List.of(new OrderLineItem(saveMenu.getId(), 1))));

            // then
            assertThat(extract).isNotNull();
        }

        @Test
        void 요청시_주문할_주문_아이템을_입력하지_않으면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when & then
            assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), null)));
        }

        @Test
        void 요청시_등록되지_않은_메뉴로_주문_아이템을_입력하면_예외가_발생한다() {
            // given
            menuGroupService.create(new MenuGroup("1인 메뉴"));
            productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Long notRegisterMenuId = 100L;
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when & then
            assertThatThrownBy(
                () -> orderService.create(new Order(orderTable.getId(), LocalDateTime.now(),
                    List.of(new OrderLineItem(notRegisterMenuId, 1)))))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청시_존재하지_않는_주문_테이블로_요청하는_경우_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final Menu saveMenu = menuService.create(createMenu);
            final Long notRegisterOrderTableId = 100L;

            // when & then
            assertThatThrownBy(() -> orderService.create(new Order(notRegisterOrderTableId, LocalDateTime.now(),
                List.of(new OrderLineItem(saveMenu.getId(), 1)))))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_리스트_조회 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final Menu saveMenu = menuService.create(createMenu);
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

            // when
            final List<Order> extract = orderService.list();

            // then
            assertThat(extract).hasSize(1);
        }
    }

    @Nested
    class 주문_상태_변경 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final Menu saveMenu = menuService.create(createMenu);
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

            // when
            final Order extract = orderService.changeOrderStatus(order.getId(),
                new Order(orderTable.getId(), OrderStatus.MEAL.name()));

            // then
            assertThat(extract.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 요청시_주문이_완료_상태이면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final Menu saveMenu = menuService.create(createMenu);
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));
            orderService.changeOrderStatus(order.getId(), new Order(orderTable.getId(), OrderStatus.COMPLETION.name()));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order(orderTable.getId(), OrderStatus.MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청시_존재하지_않는_주문_상태로_변경_요청하는_경우_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final Menu saveMenu = menuService.create(createMenu);
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final Order order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order(orderTable.getId(), "Not Registered OrderStatus")))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
