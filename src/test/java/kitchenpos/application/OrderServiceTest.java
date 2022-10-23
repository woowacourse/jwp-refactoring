package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private  TableService tableService;

    @Autowired
    private ProductService productService;

    @Test
    void 주문을_생성할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        주문_항목을_생성한다(orderLineItems, order);

        Order targetOrder = orderService.create(order);

        assertAll(
                () -> assertThat(targetOrder.getId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderStatus()).isEqualTo(order.getOrderStatus()),
                () -> assertThat(targetOrder.getOrderTableId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderLineItems().size()).isEqualTo(2)
        );
    }

    @Test
    void 주문을_생성할_때_주문_항목이_없으면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목_수와_메뉴_수가_같지_않으면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        Menu ramen = 라면_메뉴를_생성한다("라면");
        orderLineItems.add(new OrderLineItem(order.getId(), ramen.getId(), 1));
        orderLineItems.add(new OrderLineItem(order.getId(), ramen.getId(), 1));

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문에_적혀있는_주문_테이블이_존재하지_않으면_예외가_발생한다() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(null, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        주문_항목을_생성한다(orderLineItems, order);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_테이블이_비어있지_않으면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, true));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        주문_항목을_생성한다(orderLineItems, order);

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        주문_항목을_생성한다(orderLineItems, order);

        orderService.create(order);

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.size()).isOne(),
                () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus())
        );
    }

    @Test
    void 주문을_변경할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        주문_항목을_생성한다(orderLineItems, order);

        Order savedOrder = orderService.create(order);

        Order targetOrder = orderService.changeOrderStatus(savedOrder.getId(), new Order(orderTable.getId(),
                OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems));

        assertThat(targetOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 존재하지_않는_주문을_변경하려_하면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        주문_항목을_생성한다(orderLineItems, order);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(null, new Order(orderTable.getId(),
                        OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems))
        );
    }

    @Test
    void 배달이_완료된_상태에서_주문을_변경하려_하면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, false));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order order = new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);
        주문_항목을_생성한다(orderLineItems, order);

        Order targetOrder = orderService.create(order);
        orderService.changeOrderStatus(targetOrder.getId(), new Order(orderTable.getId(),
                OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(targetOrder.getId(), new Order(orderTable.getId(),
                        OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems))
        );
    }

    private void 주문_항목을_생성한다(List<OrderLineItem> orderLineItems, Order order) {
        Menu ramen = 라면_메뉴를_생성한다("라면");
        Menu chapagetti = 라면_메뉴를_생성한다("짜파게티");
        orderLineItems.add(new OrderLineItem(order.getId(), ramen.getId(), 1));
        orderLineItems.add(new OrderLineItem(order.getId(), chapagetti.getId(), 1));
    }

    private Menu 라면_메뉴를_생성한다(String name) {
        Product product = productService.create(new Product("맛있는 라면", new BigDecimal(1300)));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(null, product.getId(), 1));

        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("면"));
        Menu ramen = menuService.create(new Menu(name, new BigDecimal(1200), menuGroup.getId(), menuProducts));
        menuProducts.get(0).setMenuId(ramen.getId());

        return ramen;
    }
}
