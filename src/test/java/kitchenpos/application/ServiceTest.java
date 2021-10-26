package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql("classpath:truncate.sql")
@ActiveProfiles("test")
@Transactional
public class ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    protected Order order() {
        Menu menu = menuService.create(menu());
        OrderLineItem orderLineItem = new OrderLineItem(menu, 2);
        List<OrderLineItem> items = new ArrayList<>();
        items.add(orderLineItem);
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable createdOrderTable = tableService.create(orderTable);

        return new Order(createdOrderTable, items);
    }

    protected Menu menu() {
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);
        Product product = new Product("product", BigDecimal.valueOf(1000));
        Product createdProduct = productService.create(product);
        MenuProduct menuProduct = new MenuProduct(createdProduct, 10);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

        return new Menu("menu", BigDecimal.valueOf(5000), createdMenuGroup, menuProducts);
    }

}
