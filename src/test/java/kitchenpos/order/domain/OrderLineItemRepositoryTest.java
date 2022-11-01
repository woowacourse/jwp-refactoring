package kitchenpos.order.domain;

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
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderLineItemRepositoryTest {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

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
    @DisplayName("OrderLineItem을 저장한다.")
    void save() {
        Menu menu = createMenu();
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        Order order = createOrder(orderLineItems);

        OrderLineItem orderLineItem = orderLineItemRepository.save(new OrderLineItem(order.getId(), menu.getId(), 1));
        assertThat(orderLineItem).isEqualTo(orderLineItemRepository.findById(orderLineItem.getSeq()).orElseThrow());
    }

    @Test
    @DisplayName("모든 OrderLineItem을 조회한다.")
    void findAll() {
        Menu menu = createMenu();
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        createOrder(orderLineItems);

        List<OrderLineItem> foundOrderLineItems = orderLineItemRepository.findAll();
        assertThat(foundOrderLineItems).hasSize(2);
    }

    @Test
    @DisplayName("Order에 포함된 모든 OrderLineItem을 조회한다.")
    void findAllByOrderId() {
        Menu menu = createMenu();

        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                new OrderLineItem(menu.getId(), 2));
        Order order = createOrder(orderLineItems);

        List<OrderLineItem> foundOrderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
        assertThat(foundOrderLineItems).hasSize(2);
    }

    private Menu createMenu() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product1 = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productRepository.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        List<MenuProduct> menuProducts = List.of(new MenuProduct(null, product1.getId(), 1),
                new MenuProduct(null, product2.getId(), 1));
        return menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId(), menuProducts));
    }

    private Order createOrder(final List<OrderLineItem> orderLineItems) {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));
        return orderRepository.save(
                new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));
    }
}
