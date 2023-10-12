package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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
    protected JdbcTemplateProductDao productDao;
    @Autowired
    protected JdbcTemplateMenuDao menuDao;
    @Autowired
    protected JdbcTemplateMenuGroupDao menuGroupDao;
    @Autowired
    protected JdbcTemplateMenuProductDao menuProductDao;
    @Autowired
    protected JdbcTemplateOrderDao orderDao;
    @Autowired
    protected JdbcTemplateOrderLineItemDao orderLineItemDao;
    @Autowired
    protected JdbcTemplateTableGroupDao tableGroupDao;
    @Autowired
    protected JdbcTemplateOrderTableDao orderTableDao;


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
        orderTable.setTableGroupId(savedTableGroup.getId());

        savedOrderTable = orderTableDao.save(orderTable);
    }

    private void setupTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        savedTableGroup = tableGroupDao.save(tableGroup);
    }

    private void setupOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setQuantity(1L);

        savedOrderLineItem = orderLineItemDao.save(orderLineItem);
    }

    private void setupOrder() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus("COOKING");

        savedOrder = orderDao.save(order);
    }

    private void setupProduct() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000L));
        product.setName("productName");

        savedProduct = productDao.save(product);
    }

    private void setupMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroupName");

        savedMenuGroup = menuGroupDao.save(menuGroup);
    }

    private void setupMenu() {
        Menu menu = new Menu();
        menu.setName("menuName");
        menu.setPrice(BigDecimal.valueOf(2000L));
        menu.setMenuGroupId(savedMenuGroup.getId());

        savedMenu = menuDao.save(menu);
    }

    private void setupMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(2L);
        menuProduct.setMenuId(savedMenu.getId());

        savedMenuProduct = menuProductDao.save(menuProduct);
    }
}
