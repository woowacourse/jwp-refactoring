package kitchenpos.application.integration;

import kitchenpos.application.ProductService;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/db/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ApplicationIntegrationTest {
    @Autowired
    protected ProductService productService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected MenuGroupService menuGroupService;
//    @Autowired
//    protected OrderService orderService;
//    @Autowired
//    protected TableGroupService tableGroupService;
//    @Autowired
//    protected TableService tableService;


//    protected Product createProduct(final String name, final BigDecimal price) {
//        return productService.create(new Product(name, price));
//    }
//
//    protected MenuGroup createMenuGroup(final String name) {
//        return menuGroupService.create(new MenuGroup(name));
//    }

//    protected Menu createMenu(final String name, final Money price) {
//        final MenuGroup menuGroup = createMenuGroup("치킨");
//        final Product product1 = createProduct("후라이드", BigDecimal.valueOf(16000));
//        final Product product2 = createProduct("양념치킨", BigDecimal.valueOf(16000));
//        final MenuProduct menuProduct1 = new MenuProduct(product1.getId(), 1);
//        final MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 1);
//        final List<MenuProduct> products = List.of(menuProduct1, menuProduct2);
//        return menuService.create(new Menu(name, price, menuGroup.getId(), products));
//    }
//
//    protected Order createOrder(final OrderTable orderTableId) {
//        final Menu menu = createMenu("후라이드", Money.valueOf(16000));
//        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
//        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
//        return orderService.create(new Order(orderTableId, orderLineItems));
//    }
//
//    protected OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
//        return tableService.create(new OrderTable(numberOfGuests, empty));
//    }
}