package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql("/truncate.sql")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public abstract class IntegrationTest {
    @Autowired
    protected MenuDao menuDao;
    @Autowired
    protected MenuGroupDao menuGroupDao;
    @Autowired
    protected MenuProductDao menuProductDao;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected OrderLineItemDao orderLineItemDao;
    @Autowired
    protected OrderTableDao orderTableDao;
    @Autowired
    protected ProductDao productDao;
    @Autowired
    protected TableGroupDao tableGroupDao;

    protected MenuGroup generateMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupDao.save(menuGroup);
    }

    protected Menu generateMenu(final String name) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(220000));
        menu.setMenuGroupId(generateMenuGroup(name + "-group").getId());
        return menuDao.save(menu);
    }

    protected MenuProduct generateMenuProduct(final String menuName, final String productName) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(generateMenu(menuName).getId());
        menuProduct.setProductId(generateProduct(productName).getId());
        return menuProductDao.save(menuProduct);
    }

    protected Product generateProduct(final String name) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(16000));
        return productDao.save(product);
    }

    protected Product generateProduct(final String name, final Long price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return productDao.save(product);
    }

    protected OrderLineItem generateOrderLineItem() {
        return generateOrderLineItem(generateMenu("chicken"));
    }

    protected OrderLineItem generateOrderLineItem(final Menu menu) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setOrderId(generateOrder(OrderStatus.COOKING).getId());
        orderLineItem.setQuantity(3L);
        return orderLineItemDao.save(orderLineItem);
    }

    protected Order generateOrder(final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(generateOrderTable(3).getId());
        return orderDao.save(order);
    }

    protected OrderTable generateOrderTable(final int numberOfGuests) {
        return generateOrderTable(numberOfGuests, false);
    }

    protected OrderTable generateOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(generateTableGroup().getId());
        return orderTableDao.save(orderTable);
    }

    protected TableGroup generateTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroupDao.save(tableGroup);
    }
}
