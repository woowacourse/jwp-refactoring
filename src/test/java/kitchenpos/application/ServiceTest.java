package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/init_schema.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuService menuService;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductDao productDao;

    protected MenuProduct 메뉴_상품을_생성한다(String productName, int productPrice, Long quantity) {
        return 메뉴_상품을_생성한다(null, productName, productPrice, quantity);
    }

    protected MenuProduct 메뉴_상품을_생성한다(Long id, String productName, int productPrice, Long quantity) {
        Product product = 상품을_저장한다(productName, productPrice);
        return new MenuProduct(id, product.getId(), quantity);
    }

    protected Product 상품을_저장한다(String name, int price) {
        Product product = new Product(name, BigDecimal.valueOf(price));
        return productDao.save(product);
    }

    protected MenuGroup 메뉴_그룹을_저장한다(String name) {
        MenuGroup menuGroup1 = new MenuGroup(name);
        return menuGroupService.create(menuGroup1);
    }

    protected Menu 메뉴를_저장한다(String menuName) {
        return 메뉴를_저장한다(menuName, "상품 1", "상품 2");
    }

    protected Menu 메뉴를_저장한다(String menuName, String... menuProductNames) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (String name : menuProductNames) {
            menuProducts.add(메뉴_상품을_생성한다(name, 5000, 1L));
        }

        MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

        Menu menu = new Menu(
                menuName, BigDecimal.valueOf(menuProducts.size() * 5000L), menuGroup.getId(), menuProducts);

        return menuService.create(menu);
    }

    protected OrderTable 테이블을_저장한다(int numberOfGuests) {
        OrderTable orderTable = new OrderTable(numberOfGuests, false);
        return tableService.create(orderTable);
    }

    protected OrderTable 빈_테이블을_저장한다() {
        OrderTable orderTable = new OrderTable(0, true);
        return tableService.create(orderTable);
    }

    protected TableGroup 테이블_그룹을_저장한다(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup(null, List.of(orderTables));
        return tableGroupService.create(tableGroup);
    }

    protected Order 주문을_저장한다() {
        return 주문을_저장한다(테이블을_저장한다(4));
    }

    protected Order 주문을_저장한다(OrderTable orderTable) {
        Menu menu = 메뉴를_저장한다("메뉴");
        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3L);

        Order order = new Order(orderTable.getId(), null, null, List.of(orderLineItem));

        return orderService.create(order);
    }
}
