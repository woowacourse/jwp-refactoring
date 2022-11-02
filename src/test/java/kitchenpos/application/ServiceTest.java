package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.product.application.ProductService;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menugroup.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.ordertable.dto.request.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.request.OrderTableIdRequest;
import kitchenpos.ordertable.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.ordertable.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.order.dto.request.OrderUpdateStatusRequest;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
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

    protected TableGroup saveAndGetTableGroup() {
        return tableGroupDao.save(new TableGroup(null, LocalDateTime.now()));
    }

    protected OrderTable saveAndGetOrderTable() {
        return saveAndGetOrderTable(true);
    }

    protected OrderTable saveAndGetOrderTable(final boolean empty) {
        return orderTableDao.save(new OrderTable(null, null, 1, empty));
    }

    protected Menu saveAndGetMenu(final Long menuGroupId) {
        final Menu menu = menuDao.save(new Menu(
            null, "후라이드치킨", BigDecimal.valueOf(16_000L), menuGroupId));

        final Product product = saveAndGetProduct();
        saveAndGetMenuProduct(menu.getId(), product.getId());

        return menu;
    }

    protected MenuGroup saveAndGetMenuGroup() {
        return menuGroupDao.save(new MenuGroup(null, "한마리메뉴"));
    }

    protected Product saveAndGetProduct() {
        return productDao.save(new Product(null, "후라이드", BigDecimal.valueOf(16_000L)));
    }

    protected MenuProduct saveAndGetMenuProduct(final Long menuId, final Long productId) {
        return menuProductDao.save(new MenuProduct(null, menuId, productId, 1));
    }

    private OrderLineItem saveAndGetOrderLineItem(final Long menuId, final Long orderId) {
        final Menu menu = menuDao.findById(menuId)
            .orElseThrow();

        return orderLineItemDao.save(new OrderLineItem(null, orderId, menuId, 1,
            menu.getName(), menu.getPrice()));
    }

    protected Order saveAndGetOrder() {
        return saveAndGetOrder(OrderStatus.COOKING.name());
    }

    protected Order saveAndGetOrder(final String status) {
        final MenuGroup menuGroup = saveAndGetMenuGroup();
        final Menu menu = saveAndGetMenu(menuGroup.getId());

        final OrderTable orderTable = saveAndGetOrderTable();
        saveAndGetTableGroup();

        final Order order = orderDao.save(new Order(
            null, orderTable.getId(), status, LocalDateTime.now()));
        saveAndGetOrderLineItem(menu.getId(), order.getId());

        return orderDao.save(order);
    }

    protected ProductCreateRequest createProductCreateRequest(final BigDecimal price) {
        return new ProductCreateRequest("후라이드", price);
    }

    protected static MenuGroupCreateRequest createMenuGroupCreateRequest(final String name) {
        return new MenuGroupCreateRequest(name);
    }

    protected static MenuProductCreateRequest createMenuProductCreateRequest(final Long productId,
        final long quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }

    protected MenuCreateRequest createMenuCreateRequest(final BigDecimal price) {
        final Product product = saveAndGetProduct();
        final MenuProductCreateRequest menuProductRequest =
            createMenuProductCreateRequest(product.getId(), 1);
        final List<MenuProductCreateRequest> menuProducts = List.of(menuProductRequest);

        return createMenuCreateRequest(
            "후라이드", price, saveAndGetMenuGroup().getId(), menuProducts);
    }

    protected static MenuCreateRequest createMenuCreateRequest(final String name,
        final BigDecimal price, final Long menuGroupId,
        final List<MenuProductCreateRequest> menuProducts) {
        return new MenuCreateRequest(name, price, menuGroupId, menuProducts);
    }

    protected TableGroupCreateRequest createTableGroupCreateRequest(
        final List<OrderTableIdRequest> orderTableIdRequest) {
        return new TableGroupCreateRequest(orderTableIdRequest);
    }

    protected OrderTableCreateRequest createOrderTableCreateRequest(
        final int numberOfGuests, final boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    protected OrderTableUpdateEmptyRequest createOrderTableUpdateRequest(
        final boolean empty) {
        return new OrderTableUpdateEmptyRequest(empty);
    }

    protected OrderTableUpdateNumberOfGuestsRequest createOrderTableUpdateRequest(
        final int numberOfGuests) {
        return new OrderTableUpdateNumberOfGuestsRequest(numberOfGuests);
    }

    protected OrderLineItemCreateRequest createOrderLineItemCreateRequest(
        final Long menuId, final int quantity) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }

    protected OrderCreateRequest createOrderCreateRequest(
        final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItem) {
        return new OrderCreateRequest(orderTableId, orderLineItem);
    }

    protected OrderUpdateStatusRequest createOrderUpdateStatusRequest(
        final OrderStatus orderStatus) {
        return new OrderUpdateStatusRequest(orderStatus.name());
    }
}
