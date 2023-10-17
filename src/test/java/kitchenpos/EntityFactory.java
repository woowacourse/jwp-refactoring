package kitchenpos;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.MenuGroupDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;

@Component
public class EntityFactory {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private TableGroupDao tableGroupDao;

    public OrderTable saveOrderTable() {
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);
        request.setEmpty(true);

        return orderTableDao.save(request);
    }

    public OrderTable saveOrderTableWithNotEmpty() {
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);

        return orderTableDao.save(request);
    }

    public OrderTable saveOrderTableWithTableGroup(final TableGroup tableGroup) {
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);
        request.setTableGroupId(tableGroup.getId());

        return orderTableDao.save(request);
    }

    public TableGroup saveTableGroup(final OrderTable orderTable1, final OrderTable orderTable2) {
        final TableGroup request = new TableGroup();
        request.setOrderTables(List.of(orderTable1, orderTable2));

        return tableGroupService.create(request);
    }

    public TableGroup saveTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroupDao.save(tableGroup);
    }

    public Order saveOrder(final OrderTable orderTable) {
        final Menu menu = saveMenu();
        final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

        final Order request = new Order();
        request.setOrderTableId(orderTable.getId());
        request.setOrderLineItems(List.of(orderLineItem));

        return orderService.create(request);
    }

    public Order saveOrder() {
        final OrderTable orderTable = saveOrderTableWithNotEmpty();
        final Menu menu = saveMenu();
        final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

        final Order request = new Order();
        request.setOrderTableId(orderTable.getId());
        request.setOrderLineItems(List.of(orderLineItem));

        return orderService.create(request);
    }

    public OrderLineItem createOrderLineItem(final Menu menu, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public Menu saveMenu() {
        final Product product = saveProduct("연어", 4000);
        final MenuProduct menuProduct = createMenuProduct(4, product);
        final MenuGroup menuGroup = saveMenuGroup("일식");

        final Menu request = new Menu();
        request.setMenuGroupId(menuGroup.getId());
        request.setPrice(BigDecimal.valueOf(16000));
        request.setName("떡볶이 세트");
        request.setMenuProducts(singletonList(menuProduct));

        return menuService.create(request);
    }

    public Product saveProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return productDao.save(product);
    }

    public MenuProduct createMenuProduct(final int quantity, final Product product) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroupDao.save(menuGroup);
    }
}
