package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문 등록")
    @Test
    void create() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));

        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        final var cokeInMenu = new MenuProduct(coke.getId(), 2);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaMenu = menuDao.save(
                new Menu("싼 피자", new BigDecimal(9_000), italian.getId(), List.of(pizzaInMenu)));
        final var cokeMenu = menuDao.save(
                new Menu("싼 콜라", new BigDecimal(900), italian.getId(), List.of(cokeInMenu)));

        final var pizzaOrderItem = new OrderLineItem(pizzaMenu.getId(), 2);
        final var cokeOrderItem = new OrderLineItem(cokeMenu.getId(), 2);

        final var table = orderTableDao.save(new OrderTable(2));

        final var order = new Order(table.getId(), List.of(pizzaOrderItem, cokeOrderItem));

        final var result = orderService.create(order);
        assertThat(result).isEqualTo(order);
    }

    @DisplayName("주문 항목이 비어있다면 등록 시 예외 발생")
    @Test
    void create_emptyOrderLineItems_throwsException() {
        final var table = orderTableDao.save(new OrderTable(2));

        final var order = new Order(table.getId(), Collections.emptyList());

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴가 중복된다면 등록 시 예외 발생")
    @Test
    void create_duplicatedMenuInOrderLineItems_throwsException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaMenu = menuDao.save(
                new Menu("싼 피자", new BigDecimal(9_000), italian.getId(), List.of(pizzaInMenu)));

        final var onePizzaOrderItem = new OrderLineItem(pizzaMenu.getId(), 1);
        final var twoPizzasOrderItem = new OrderLineItem(pizzaMenu.getId(), 2);

        final var table = orderTableDao.save(new OrderTable(2));

        final var order = new Order(table.getId(), List.of(onePizzaOrderItem, twoPizzasOrderItem));

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 상태라면 등록 시 예외 발생")
    @Test
    void create_orderTableIsEmptyTrue_throwsException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaMenu = menuDao.save(
                new Menu("싼 피자", new BigDecimal(9_000), italian.getId(), List.of(pizzaInMenu)));

        final var orderItem = new OrderLineItem(pizzaMenu.getId(), 1);

        final var emptyTable = new OrderTable(2);
        emptyTable.setEmpty(true);
        orderTableDao.save(emptyTable);

        final var order = new Order(emptyTable.getId(), List.of(orderItem));

        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaMenu = menuDao.save(
                new Menu("싼 피자", new BigDecimal(9_000), italian.getId(), List.of(pizzaInMenu)));

        final var orderItem = new OrderLineItem(pizzaMenu.getId(), 1);

        final var table = orderTableDao.save(new OrderTable(2));

        final var saved = orderService.create(new Order(table.getId(), List.of(orderItem)));
        final var changed = new Order(table.getId(), List.of(orderItem));
        changed.setOrderStatus(OrderStatus.MEAL.name());

        final var result = orderService.changeOrderStatus(saved.getId(), changed);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(saved.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(changed.getOrderStatus())
        );
    }

    @DisplayName("주문 상태가 COMPLETION이라면, 주문 상태 변경 시 예외 발생")
    @Test
    void changeOrderStatus_orderStatusIsCompletion_throwsException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaMenu = menuDao.save(
                new Menu("싼 피자", new BigDecimal(9_000), italian.getId(), List.of(pizzaInMenu)));

        final var orderItem = new OrderLineItem(pizzaMenu.getId(), 1);

        final var table = orderTableDao.save(new OrderTable(2));

        final var order = new Order(table.getId(), List.of(orderItem));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.create(order);

        final var changed = new Order(table.getId(), List.of(orderItem));
        changed.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), changed)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
