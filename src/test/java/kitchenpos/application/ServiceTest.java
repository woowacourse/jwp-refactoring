package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/tear_down.sql")
@SpringBootTest
abstract class ServiceTest {

    protected SoftAssertions softly;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        softly = new SoftAssertions();
    }

    protected Product saveProduct(final String name) {
        return saveProduct(name, BigDecimal.valueOf(10_000));
    }

    protected Product saveProduct(final String name, final BigDecimal price) {
        final Product product = Product.of(name, price);
        return productRepository.save(product);
    }

    protected MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu saveMenu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                            final MenuProduct... menuProducts) {
        final List<Product> products = Arrays.stream(menuProducts)
                .map(MenuProduct::getProductId)
                .map(it -> productRepository.getProduct(it))
                .collect(Collectors.toList());
        final MenuProductService menuProductService = MenuProductService.of(products, List.of(menuProducts));
        final Menu menu = Menu.of(name, price, menuGroup.getId(), List.of(menuProducts), menuProductService);
        return menuRepository.save(menu);
    }

    protected OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.of(numberOfGuests, empty);
        return orderTableRepository.save(orderTable);
    }

    protected Order saveOrder(final OrderTable orderTable, final String orderStatus,
                              final OrderLineItem... orderLineItems) {
        final Order order = new Order(
                orderTable.getId(), orderStatus,
                LocalDateTime.now(),
                toNewOrderLineItems(orderLineItems)
        );
        return orderRepository.save(order);
    }

    private List<OrderLineItem> toNewOrderLineItems(final OrderLineItem[] oldOrderLineItems) {
        final List<OrderLineItem> newOrderLineItems = new ArrayList<>();
        for (final OrderLineItem old : oldOrderLineItems) {
            final Menu menu = menuRepository.findById(old.getMenuId())
                    .orElseThrow();
            final MenuGroup menuGroup = menuGroupRepository.findById(menu.getMenuGroupId())
                    .orElseThrow();
            final OrderMenu orderMenu = OrderMenu.from(menu);
            final OrderLineItem orderLineItem = new OrderLineItem(old.getQuantity(), orderMenu);
            newOrderLineItems.add(orderLineItem);
        }
        return newOrderLineItems;
    }

    protected TableGroup saveTableGroup(final OrderTable... orderTables) {
        final TableGroup tableGroup = TableGroup.from(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable orderTable : orderTables) {
            final OrderTable saved = new OrderTable(orderTable.getId(), savedTableGroup.getId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
            orderTableRepository.save(saved);
        }

        return savedTableGroup;
    }
}
