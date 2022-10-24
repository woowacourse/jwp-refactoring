package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/init_schema.sql")
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

    protected OrderTable 테이블을_생성한다(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }

    protected MenuProduct 메뉴_상품을_생성한다(String productName, int productPrice, Long quantity) {
        Product product = 상품을_저장한다(productName, productPrice);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected Product 상품을_저장한다(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return productService.create(product);
    }

    protected MenuGroup 메뉴_그룹을_저장한다(String name) {
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName(name);

        return menuGroupService.create(menuGroup1);
    }

    protected Menu 메뉴를_저장한다(String menuName) {
        MenuProduct menuProduct1 = 메뉴_상품을_생성한다("상품 1", 1000, 1L);
        MenuProduct menuProduct2 = 메뉴_상품을_생성한다("상품 2", 2000, 1L);
        MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setPrice(BigDecimal.valueOf(3000));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        return menuService.create(menu);
    }

    protected OrderTable 테이블을_저장한다(int numberOfGuests) {
        OrderTable orderTable = 테이블을_생성한다(numberOfGuests, false);

        return tableService.create(orderTable);
    }

    protected OrderTable 빈_테이블을_저장한다() {
        OrderTable orderTable = 테이블을_생성한다(0, true);

        return tableService.create(orderTable);
    }

    protected TableGroup 테이블_그룹을_저장한다(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTables));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        return savedTableGroup;
    }

    protected Order 주문을_저장한다() {
        return 주문을_저장한다(테이블을_저장한다(4));
    }

    protected Order 주문을_저장한다(OrderTable orderTable) {
        Menu menu = 메뉴를_저장한다("메뉴");
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(3L);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        return orderService.create(order);
    }
}
