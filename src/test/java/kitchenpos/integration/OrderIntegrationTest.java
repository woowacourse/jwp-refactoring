package kitchenpos.integration;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문_전체_조회_시_주문_항목을_같이_조회할_수_있다() {
        Product product = productDao.save(new Product("상품", BigDecimal.valueOf(1000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
        Menu menu = menuDao.save(new Menu(null, "메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                new MenuProduct(null, product, 1)
        )));
        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(menu.getId(), 1)
        );
        OrderTable orderTable = orderTableDao.save(new OrderTable(3, false));

        Order order = new Order(orderTable, orderLineItems);
        Order saved = orderService.create(order);

        assertAll(
                () -> assertThat(orderService.list()).hasSize(1),
                () -> assertThat(orderService.list().get(0).getOrderLineItems()).hasSize(1)
        );
    }

    @Nested
    class 주문을_저장할_때 {

        @Test
        void 주문_항목이_없으면_저장할_수_없다() {
            List<OrderLineItem> orderLineItems = List.of();
            OrderTable orderTable = new OrderTable(3, false);
            assertThatThrownBy(() -> orderService.create(new Order(orderTable, orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 항목이 비어있으면 생성할 수 없다.");
        }

        @Test
        void 메뉴가_존재하지_않는_주문항목이_있는_주문은_저장할_수_없다() {
            Product product = productDao.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuDao.save(new Menu(null, "메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(null, product, 1)
            )));
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1),
                    new OrderLineItem(menu.getId(), 1)
            );
            OrderTable orderTable = new OrderTable(3, false);
            assertThatThrownBy(() -> orderService.create(new Order(orderTable, orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("한번의 주문에서 중복 메뉴를 주문할 수 없습니다.");
        }

        @Test
        void 빈_주문_테이블에서_주문할_수_없다() {
            Product product = productDao.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuDao.save(new Menu(null, "메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(null, product, 1)
            )));
            Menu menu2 = menuDao.save(new Menu(null, "메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(null, product, 1)
            )));
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1),
                    new OrderLineItem(menu2.getId(), 1)
            );
            OrderTable orderTable = new OrderTable(3L, 3, true);

            assertThatThrownBy(() -> orderService.create(new Order(orderTable, orderLineItems)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블이 비어있으면 생성할 수 없다.");
        }

        @Test
        void 존재하지_않는_테이블에서_주문할_수_없다() {
            Product product = productDao.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuDao.save(
                    new Menu(null, "메뉴", BigDecimal.valueOf(1000), menuGroup.getId(),
                            List.of(new MenuProduct(null, product, 1)))
            );
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1)
            );
            OrderTable orderTable = new OrderTable(4L, 3, false);

            Order order = new Order(orderTable, orderLineItems);
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 테이블이 존재하지 않습니다.");
        }

        @Test
        void 요리중_상태로_변경한다() {
            Product product = productDao.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuDao.save(new Menu(null, "메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(null, product, 1)
            )));
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1)
            );
            OrderTable orderTable = orderTableDao.save(new OrderTable(3, false));

            Order order = new Order(orderTable, orderLineItems);
            Order saved = orderService.create(order);


            assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }
    }
}
