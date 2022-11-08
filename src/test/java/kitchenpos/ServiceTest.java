package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql("/schema.sql")
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

    protected OrderTable saveAndGetNotEmptyOrderTable(final Long id) {
        return orderTableDao.save(new OrderTable(id, null, 4, false));
    }

    protected OrderTable saveAndGetNotEmptyOrderTableInGroup(final Long id, final Long tableGroupId) {
        return orderTableDao.save(new OrderTable(id, tableGroupId, 4, false));
    }

    protected OrderTable saveAndGetOrderTable(final Long id, final boolean empty) {
        return orderTableDao.save(new OrderTable(id, null, 1, empty));
    }

    protected Menu saveAndGetMenu(final Long id) {
        saveAndGetMenuGroup(1L);
        final MenuProducts menuProducts = new MenuProducts(new ArrayList<>());
        return menuDao.save(new Menu(id, "피자세트메뉴", BigDecimal.valueOf(15_000L), 1L, menuProducts));
    }

    protected MenuGroup saveAndGetMenuGroup(final Long id) {
        return menuGroupDao.save(new MenuGroup(id, "애기메뉴목록"));
    }

    protected Product saveAndGetProduct(final Long id) {
        return productDao.save(new Product(id, "하와이안피자", BigDecimal.valueOf(16_000L)));
    }

    protected MenuProduct saveAndGetMenuProduct(final Long id, final Long menuId, final Long productId) {
        return menuProductDao.save(new MenuProduct(id, menuId, productId, 1));
    }

    protected OrderLineItem saveAndGetOrderLineItem(final Long id, final Long menuId, final Long orderId) {
        return orderLineItemDao.save(new OrderLineItem(id, orderId, menuId, 1));
    }

    protected Order saveAndGetOrder(final Long id, final String status) {
        final MenuGroup menuGroup = saveAndGetMenuGroup(1L);
        final MenuProducts menuProducts = new MenuProducts(new ArrayList<>());
        final Menu menu = new Menu(1L, "치킨메뉴", BigDecimal.valueOf(20_000L), menuGroup.getId(), menuProducts);

        final Product product = saveAndGetProduct(1L);
        final MenuProduct menuProduct = saveAndGetMenuProduct(1L, menu.getId(), product.getId());
        menu.addMenuProduct(menuProduct);
        menuDao.save(menu);

        final OrderTable orderTable = saveAndGetNotEmptyOrderTable(1L);
        final OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<>());
        final Order order = new Order(id, orderTable.getId(), status, LocalDateTime.now(), orderLineItems);
        order.addOrderLineItem(saveAndGetOrderLineItem(1L, 1L, order.getId()));
        return orderDao.save(order);
    }

    protected Order saveAndGetOrderInOrderTable(final Long id, final OrderTable orderTable, final String status) {
        final MenuGroup menuGroup = saveAndGetMenuGroup(1L);
        final MenuProducts menuProducts = new MenuProducts(new ArrayList<>());
        final Menu menu = new Menu(1L, "치킨메뉴", BigDecimal.valueOf(20_000L), menuGroup.getId(), menuProducts);

        final Product product = saveAndGetProduct(1L);
        final MenuProduct menuProduct = saveAndGetMenuProduct(1L, menu.getId(), product.getId());
        menu.addMenuProduct(menuProduct);
        menuDao.save(menu);

        final OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<>());
        final Order order = new Order(id, orderTable.getId(), status, LocalDateTime.now(), orderLineItems);
        order.addOrderLineItem(saveAndGetOrderLineItem(1L, 1L, order.getId()));
        return orderDao.save(order);
    }
}
