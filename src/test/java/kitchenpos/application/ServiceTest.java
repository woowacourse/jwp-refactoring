package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createMenu;
import static kitchenpos.fixture.DomainCreator.createMenuGroup;
import static kitchenpos.fixture.DomainCreator.createMenuProduct;
import static kitchenpos.fixture.DomainCreator.createOrder;
import static kitchenpos.fixture.DomainCreator.createOrderLineItem;
import static kitchenpos.fixture.DomainCreator.createOrderTable;
import static kitchenpos.fixture.DomainCreator.createProduct;
import static kitchenpos.fixture.DomainCreator.createTableGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.ProductCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    protected MenuGroup saveMenuGroup(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Menu saveMenu(final Menu menu) {
        return menuDao.save(menu);
    }

    protected OrderLineItem saveOrderLineItem(final OrderLineItem orderLineItem) {
        return orderLineItemDao.save(orderLineItem);
    }

    protected Product saveProduct(final Product product) {
        return productDao.save(product);
    }

    protected MenuProduct saveMenuProduct(final MenuProduct menuProduct) {
        return menuProductDao.save(menuProduct);
    }

    protected OrderTable saveOrderTable(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    protected Order saveOrder(final Order order) {
        return orderDao.save(order);
    }

    protected TableGroup saveAndGetTableGroup() {
        return tableGroupDao.save(createTableGroup(null, new ArrayList<>()));
    }

    protected OrderTable saveAndGetOrderTable() {
        return saveOrderTable(createOrderTable(null, null, 1, true));
    }

    protected OrderTable saveAndGetOrderTable(final boolean empty) {
        return saveOrderTable(createOrderTable(null, null, 1, empty));
    }

    protected Menu saveAndGetMenu(final Long menuGroupId) {
        return saveMenu(createMenu(null, "후라이드치킨", BigDecimal.valueOf(16_000L), menuGroupId,
            new ArrayList<>()));
    }

    protected MenuGroup saveAndGetMenuGroup() {
        return saveMenuGroup(createMenuGroup(null, "한마리메뉴"));
    }

    protected Product saveAndGetProduct() {
        return saveProduct(createProduct(null, "후라이드", BigDecimal.valueOf(16_000L)));
    }

    protected MenuProduct saveAndGetMenuProduct(final Long menuId, final Long productId) {
        return saveMenuProduct(createMenuProduct(null, menuId, productId, 1));
    }

    private OrderLineItem saveAndGetOrderLineItem(final Long menuId, final Long orderId) {
        return saveOrderLineItem(createOrderLineItem(null, orderId, menuId, 1));
    }

    protected Order saveAndGetOrder() {
        final MenuGroup menuGroup = saveAndGetMenuGroup();
        final Menu menu = saveAndGetMenu(menuGroup.getId());

        final Product product = saveAndGetProduct();
        final MenuProduct menuProduct = saveAndGetMenuProduct(menu.getId(), product.getId());
        menu.setMenuProducts(List.of(menuProduct));
        menuDao.save(menu);

        final TableGroup tableGroup = saveAndGetTableGroup();
        final OrderTable orderTable = saveAndGetOrderTable();
        tableGroup.setOrderTables(List.of(orderTable));
        tableGroupDao.save(tableGroup);

        final Order order = saveOrder(createOrder(
            null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
            new ArrayList<>()));
        final OrderLineItem orderLineItem = saveAndGetOrderLineItem(menu.getId(), order.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        orderDao.save(order);

        return orderDao.save(order);
    }

    protected Order saveAndGetOrder(final String status) {
        final Order order = saveAndGetOrder();
        order.setOrderStatus(status);

        return orderDao.save(order);
    }

    protected Menu createMenuRequest(final BigDecimal price) {
        final Product product = saveAndGetProduct();
        final MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        return createMenu(null, "후라이드", price, saveAndGetMenuGroup().getId(), menuProducts);
    }

    protected ProductCreateRequest createProductCreateRequest(final BigDecimal price) {
        return new ProductCreateRequest("후라이드", price);
    }

    public static MenuGroupCreateRequest createMenuGroupCreateRequest(final String name) {
        return new MenuGroupCreateRequest(name);
    }
}
