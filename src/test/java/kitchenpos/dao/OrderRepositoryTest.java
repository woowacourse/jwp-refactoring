package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

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
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Order를 저장한다.")
    void save() {
        Menu menu = createMenu();
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        Order order = orderRepository.save(
                new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));
        assertThat(order).isEqualTo(orderRepository.findById(order.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 Order를 조회한다.")
    void findAll() {
        Menu menu = createMenu();

        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        Order order1 = orderRepository.save(
                new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));
        Order order2 = orderRepository.save(
                new Order(orderTable2.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).containsExactly(order1, order2);
    }

    @Test
    @DisplayName("OrderTableId와 OrderStatus 내에 Order가 존재하는지 확인한다.")
    void existsByOrderTableIdAndOrderStatusIn() {
        Menu menu = createMenu();

        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        orderRepository.save(new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                List.of(OrderStatus.COOKING));

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("OrderTableId 중 하나 이상의 id와 OrderStatus 중 하나 이상의 상태에 Order가 존재하는지 확인한다.")
    void existsByOrderTableIdInAndOrderStatusIn() {
        Menu menu = createMenu();

        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        orderRepository.save(new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable1.getId()),
                List.of(OrderStatus.COOKING));

        assertThat(actual).isTrue();
    }

    private Menu createMenu() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product1 = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productRepository.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        List<MenuProduct> menuProducts = List.of(new MenuProduct(null, product1.getId(), 1),
                new MenuProduct(null, product2.getId(), 1));
        return menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId(), menuProducts));
    }
}
