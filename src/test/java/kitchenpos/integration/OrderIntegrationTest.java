package kitchenpos.integration;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderService;
import kitchenpos.order.OrderStatus;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
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
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문_전체_조회_시_주문_항목을_같이_조회할_수_있다() {
        Product product = productRepository.save(new Product("상품", BigDecimal.valueOf(1000)));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
        Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                new MenuProduct(product, 1)
        )));
        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(menu.getId(), 1)
        );
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));

        Order order = new Order(orderLineItems, orderTable.getId());
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
            assertThatThrownBy(() -> orderService.create(new Order(orderLineItems, 1L)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 항목이 비어있으면 생성할 수 없다.");
        }

        @Test
        void 메뉴가_존재하지_않는_주문항목이_있는_주문은_저장할_수_없다() {
            Product product = productRepository.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(product, 1)
            )));
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1),
                    new OrderLineItem(menu.getId(), 1)
            );
            OrderTable orderTable = new OrderTable(3, false);
            assertThatThrownBy(() -> orderService.create(new Order(orderLineItems, 1L)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("한번의 주문에서 중복 메뉴를 주문할 수 없습니다.");
        }

        @Test
        void 빈_주문_테이블에서_주문할_수_없다() {
            Product product = productRepository.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(product, 1)
            )));
            Menu menu2 = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(product, 1)
            )));
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1),
                    new OrderLineItem(menu2.getId(), 1)
            );
            OrderTable orderTable = orderTableRepository.save(new OrderTable(3, true));

            assertThatThrownBy(() -> orderService.create(new Order(orderLineItems, orderTable.getId())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블이 비어있으면 주문할 수 없습니다.");
        }

        @Test
        void 존재하지_않는_테이블에서_주문할_수_없다() {
            Product product = productRepository.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuRepository.save(
                    new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(),
                            List.of(new MenuProduct(product, 1)))
            );
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1)
            );

            Order order = new Order(orderLineItems, -1L);
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 테이블이 존재하지 않습니다.");
        }

        @Test
        void 요리중_상태로_변경한다() {
            Product product = productRepository.save(new Product("상품", BigDecimal.valueOf(1000)));
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
            Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(
                    new MenuProduct(product, 1)
            )));
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(menu.getId(), 1)
            );
            OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));

            Order order = new Order(orderLineItems, orderTable.getId());
            Order saved = orderService.create(order);


            assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }
    }
}
