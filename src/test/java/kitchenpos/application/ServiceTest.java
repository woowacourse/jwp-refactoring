package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
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
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        softly = new SoftAssertions();
    }

    protected Product saveProduct(final String name) {
        return saveProduct(name, BigDecimal.ONE);
    }

    protected Product saveProduct(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return productDao.save(product);
    }

    protected MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupDao.save(menuGroup);
    }

    protected List<MenuProduct> getMenuProducts(final Pair<Product, Long>... pairs) {
        return Arrays.stream(pairs)
                .map(pair -> new MenuProduct(pair.getFirst().getId(), pair.getSecond()))
                .collect(Collectors.toList());
    }

    protected Menu saveMenu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                            final Pair<Product, Long>... menuProductPairs) {
        final List<MenuProduct> menuProducts = getMenuProducts(menuProductPairs);
        final Menu menu = new Menu(name, price, menuGroup.getId(), menuProducts);
        return menuDao.save(menu);
    }

    protected OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTableDao.save(orderTable);
    }

    protected Order saveOrder(final OrderTable orderTable, final String orderStatus,
                              final Pair<Menu, Long>... orderLineItemPairs) {
        final List<OrderLineItem> orderLineItems = Arrays.stream(orderLineItemPairs)
                .map(pair -> new OrderLineItem(pair.getFirst().getId(), pair.getSecond()))
                .collect(Collectors.toList());

        final Order order = new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);
        return orderDao.save(order);
    }

    protected TableGroup saveTableGroup(final OrderTable... orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTables));
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(savedTableGroup.getId());
            orderTableDao.save(orderTable);
        }

        return savedTableGroup;
    }
}
