package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.dao.jpa.MenuProductRepository;
import kitchenpos.dao.jpa.MenuRepository;
import kitchenpos.dao.jpa.OrderLineItemRepository;
import kitchenpos.dao.jpa.OrderRepository;
import kitchenpos.dao.jpa.OrderTableRepository;
import kitchenpos.dao.jpa.ProductRepository;
import kitchenpos.dao.jpa.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.supports.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.TestExecutionListeners;

@DisplayNameGeneration(ReplaceUnderscores.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
@TestExecutionListeners(
        value = DatabaseCleaner.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public class ServiceTestContext {

    @Autowired
    protected MenuService menuService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected ProductService productService;
    @Autowired
    protected TableService tableService;
    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected MenuRepository menuRepository;
    @Autowired
    protected MenuGroupRepository menuGroupRepository;
    @Autowired
    protected MenuProductRepository menuProductRepository;
    @Autowired
    protected OrderRepository orderDao;
    @Autowired
    protected OrderLineItemRepository orderLineItemDao;
    @Autowired
    protected TableGroupRepository tableGroupDao;
    @Autowired
    protected OrderTableRepository orderTableDao;


    protected Menu savedMenu;
    protected Product savedProduct;
    protected MenuGroup savedMenuGroup;
    protected MenuProduct savedMenuProduct;
    protected Order savedOrder;
    protected OrderTable savedOrderTable;
    protected OrderLineItem savedOrderLineItem;
    protected TableGroup savedTableGroup;

    @BeforeEach
    void setup() {
        setupProduct();
        setupMenuGroup();
        setupMenu();
        setupMenuProduct();
        setupTableGroup();
        setupOrderTable();
        setupOrder();
        setupOrderLineItem();
    }

    private void setupOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(0);
        orderTable.setTableGroup(savedTableGroup);

        savedOrderTable = orderTableDao.save(orderTable);
    }

    private void setupTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        savedTableGroup = tableGroupDao.save(tableGroup);
    }

    private void setupOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(savedMenu);
        orderLineItem.setOrder(savedOrder);
        orderLineItem.setQuantity(1L);

        savedOrderLineItem = orderLineItemDao.save(orderLineItem);
    }

    private void setupOrder() {
        Order order = new Order(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());

        savedOrder = orderDao.save(order);
    }

    private void setupProduct() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000L));
        product.setName("productName");

        savedProduct = productRepository.save(product);
    }

    private void setupMenuGroup() {
        MenuGroup menuGroup = new MenuGroup("menuGroupName");

        savedMenuGroup = menuGroupRepository.save(menuGroup);
    }

    private void setupMenu() {
        Menu menu = new Menu("menuName", BigDecimal.valueOf(2000L), savedMenuGroup);

        savedMenu = menuRepository.save(menu);
    }

    private void setupMenuProduct() {
        MenuProduct menuProduct = new MenuProduct(savedMenu, savedProduct, 2L);

        savedMenuProduct = menuProductRepository.save(menuProduct);
    }
}
