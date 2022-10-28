package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문을_생성할_수_있다() {
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));

        MenuProduct menuProduct1 = new MenuProduct(product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 2);
        MenuProduct menuProduct3 = new MenuProduct(product2.getId(), 2);
        MenuProduct menuProduct4 = new MenuProduct(product2.getId(), 2);

        Menu menu1 = menuRepository.save(new Menu("메뉴1", new BigDecimal(40000), menuGroup,
                List.of(menuProduct1, menuProduct2)));
        Menu menu2 = menuRepository.save(new Menu("메뉴2", new BigDecimal(40000), menuGroup,
                List.of(menuProduct3, menuProduct4)));

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        OrderLineItem orderLineItem1 = new OrderLineItem(null, menu1.getId(), 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, menu2.getId(), 2);

        Order order = new Order(orderTable, COOKING, List.of(orderLineItem1, orderLineItem2));

        Order actual = orderService.create(order);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getOrderStatus()).isEqualTo(COOKING);
            assertThat(actual.getOrderLineItems()).hasSize(2);
        });
    }

    @Test
    void 주문_항목이_비어있는_경우_주문을_생성할_수_없다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        Order order = new Order(orderTable, COOKING, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블인_경우_주문을_생성할_수_없다() {
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));

        MenuProduct menuProduct1 = new MenuProduct(product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 2);
        MenuProduct menuProduct3 = new MenuProduct(product2.getId(), 2);
        MenuProduct menuProduct4 = new MenuProduct(product2.getId(), 2);

        Menu menu1 = menuRepository.save(new Menu("메뉴1", new BigDecimal(40000), menuGroup,
                List.of(menuProduct1, menuProduct2)));
        Menu menu2 = menuRepository.save(new Menu("메뉴2", new BigDecimal(40000), menuGroup,
                List.of(menuProduct3, menuProduct4)));

        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, true));

        OrderLineItem orderLineItem1 = new OrderLineItem(null, menu1.getId(), 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, menu2.getId(), 2);

        Order order = new Order(orderTable, COOKING, List.of(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_주문_목록을_조회할_수_있다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        Order order1 = new Order(orderTable, COOKING, new ArrayList<>());
        Order order2 = new Order(orderTable, COOKING, new ArrayList<>());

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        Order order = orderRepository.save(new Order(orderTable, COOKING, new ArrayList<>()));
        Order newOrder = orderRepository.save(new Order(orderTable, MEAL, new ArrayList<>()));

        Order actual = orderService.changeOrderStatus(order.getId(), newOrder);

        assertThat(actual.getOrderStatus()).isEqualTo(MEAL);
    }

    @Test
    void 기존_주문_상태가_계산_완료_상태인_경우_상태를_변경할_수_없다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        Order order = orderRepository.save(new Order(orderTable, COMPLETION, new ArrayList<>()));
        Order newOrder = orderRepository.save(new Order(orderTable, MEAL, new ArrayList<>()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), newOrder)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
