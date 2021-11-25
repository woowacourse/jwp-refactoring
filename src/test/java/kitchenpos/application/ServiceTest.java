package kitchenpos.application;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.annotation.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@IntegrationTest
public class ServiceTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected TableService tableService;
    @Autowired
    private EntityManager em;

    protected Order order;
    protected Menu menu;
    protected MenuProduct menuProduct1;
    protected Product product1;
    protected MenuGroup menuGroup;
    protected OrderTable orderTable1;
    protected OrderTable orderTable2;
    protected OrderTable orderTable3;
    protected OrderTable orderTable4;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("bepozMenuGroup"));
        product1 = productRepository.save(new Product("product1", new Price(1000)));
        Product product2 = productRepository.save(new Product("product2", new Price(1000)));
        menuProduct1 = new MenuProduct(product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 1);
        menu = menuRepository.save(new Menu("menu", new Price(1000),
                menuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2)));

        orderTable1 = orderTableRepository.save(new OrderTable(10, false));
        orderTable2 = orderTableRepository.save(new OrderTable(10, false));
        orderTable3 = orderTableRepository.save(new OrderTable(20, true));
        orderTable4 = orderTableRepository.save(new OrderTable(20, true));
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1L);
        order = orderService.create(
                new Order(orderTable1.getId(), Collections.singletonList(orderLineItem))
        );

        em.flush();
        em.clear();
    }

    @AfterEach
    void tearDown() {
        orderTableRepository.deleteAll();
        menuGroupRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
        menuRepository.deleteAll();
    }

    protected List<Long> emptyTrueOrderTableIds() {
        return tableService.list().stream()
                .filter(OrderTable::isEmpty)
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    protected List<Long> emptyFalseOrderTableIds() {
        return tableService.list().stream()
                .filter(table -> !table.isEmpty())
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
