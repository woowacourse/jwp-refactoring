package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderUpdateStatusRequest;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
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
