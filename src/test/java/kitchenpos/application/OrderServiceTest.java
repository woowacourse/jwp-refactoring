package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class OrderServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    private Menu menu;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        menu = createMenu_후라이드세트();
        orderTable = createNotEmptyTable();
    }

    @Test
    void create() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(2);

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(orderTable.getId());

        Order result = orderService.create(order);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        assertThat(result.getOrderTableId()).isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("create - 빈 테이블에서 주문 시도시 예외처리")
    void create_IfTableIsEmpty_ThrowException() {
    }

    @Test
    void list() {
    }

    @Test
    void changeOrderStatus() {
    }

    private OrderTable createNotEmptyTable() {
        // create table
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        orderTable = tableService.create(orderTable);

        // change to not empty
        orderTable.setEmpty(false);
        orderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        orderTable.setNumberOfGuests(4);
        return tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
    }

    private Menu createMenu_후라이드세트() {
        // create products
        Product 후라이드치킨 = new Product();
        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setPrice(new BigDecimal(10_000));
        후라이드치킨 = productService.create(후라이드치킨);

        Product 프랜치프라이 = new Product();
        프랜치프라이.setName("프랜치프라이");
        프랜치프라이.setPrice(new BigDecimal(5_000));
        프랜치프라이 = productService.create(프랜치프라이);

        // create a menu group
        MenuGroup 세트메뉴 = new MenuGroup();
        세트메뉴.setName("세트메뉴");
        세트메뉴 = menuGroupService.create(세트메뉴);

        // create menu
        Menu menu = new Menu();
        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(13_000));
        menu.setMenuGroupId(세트메뉴.getId());
        menu.setMenuProducts(createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이)));

        return menuService.create(menu);
    }

    private List<MenuProduct> createMenuProductsWithAllQuantityAsOne(List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (Product product : products) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(1);

            menuProducts.add(menuProduct);
        }
        return Collections.unmodifiableList(menuProducts);
    }
}