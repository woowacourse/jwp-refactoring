package kitchenpos.application;

import kitchenpos.Menu.domain.Menu;
import kitchenpos.Menu.domain.MenuGroup;
import kitchenpos.Menu.domain.MenuProduct;
import kitchenpos.Menu.domain.Product;
import kitchenpos.Menu.domain.repository.MenuGroupRepository;
import kitchenpos.Menu.domain.repository.MenuRepository;
import kitchenpos.Menu.domain.repository.ProductRepository;
import kitchenpos.Order.application.OrderService;
import kitchenpos.Order.domain.Order;
import kitchenpos.Order.domain.OrderLineItem;
import kitchenpos.Order.domain.repository.OrderRepository;
import kitchenpos.OrderTable.application.TableService;
import kitchenpos.OrderTable.domain.OrderTable;
import kitchenpos.OrderTable.domain.repository.OrderTableRepository;
import kitchenpos.annotation.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
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

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("bepozMenuGroup"));
        product1 = productRepository.save(new Product("product1", BigDecimal.valueOf(1000)));
        Product product2 = productRepository.save(new Product("product2", BigDecimal.valueOf(1000)));
        menuProduct1 = new MenuProduct(product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 1);
        menu = menuRepository.save(new Menu("menu", BigDecimal.valueOf(1000),
                menuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2)));

        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, false));
        OrderTable orderTable3 = orderTableRepository.save(new OrderTable(20, true));
        OrderTable orderTable4 = orderTableRepository.save(new OrderTable(20, true));
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
