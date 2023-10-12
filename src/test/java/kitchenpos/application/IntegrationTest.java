package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
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
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class IntegrationTest {

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    protected Order 맛있는_메뉴_주문() {
        OrderTable 주문_테이블 = 주문_테이블(false);
        return 주문(주문_테이블, OrderStatus.COOKING, 맛있는_메뉴());
    }

    protected Order 식사중인_주문() {
        OrderTable 주문_테이블 = 주문_테이블(false);
        return 주문(주문_테이블, OrderStatus.MEAL, 맛있는_메뉴());
    }

    protected Order 완료된_주문() {
        OrderTable 주문_테이블 = 주문_테이블(false);
        return 주문(주문_테이블, OrderStatus.COMPLETION, 맛있는_메뉴());
    }

    protected TableGroup 빈_테이블들을_그룹으로_지정한다() {
        OrderTable orderTable1 = 주문_테이블(true);
        OrderTable orderTable2 = 주문_테이블(true);
        return 테이블_그룹(orderTable1, orderTable2);
    }

    protected TableGroup 테이블_그룹(OrderTable... 주문_테이블들) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(주문_테이블들));
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroupDao.save(tableGroup);
    }

    protected Order 주문(OrderTable orderTable, OrderStatus orderStatus, Menu... 메뉴들) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable.getId());
        List<OrderLineItem> orderLineItems = Arrays.stream(메뉴들)
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);
        return orderDao.save(order);
    }

    private OrderLineItem toOrderLineItem(Menu 메뉴) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(메뉴.getId());
        return orderLineItem;
    }

    protected OrderTable 주문_테이블(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTableDao.save(orderTable);
    }

    protected Menu 맛있는_메뉴() {
        return 메뉴(메뉴_그룹(),
                BigDecimal.valueOf(5),
                "맛있는 메뉴",
                메뉴_상품(상품("상품1", BigDecimal.valueOf(1)), 3),
                메뉴_상품(상품("상품2", BigDecimal.valueOf(2)), 2)
        );
    }

    protected Menu 메뉴(MenuGroup 메뉴_그룹, BigDecimal 가격, String 이름, MenuProduct... 메뉴_상품들) {
        Menu menu = new Menu();
        menu.setPrice(가격);
        menu.setMenuGroupId(메뉴_그룹.getId());
        menu.setName(이름);
        menu.setMenuProducts(Arrays.asList(메뉴_상품들));
        return menuDao.save(menu);
    }

    protected MenuGroup 메뉴_그룹() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        return menuGroupDao.save(menuGroup);
    }

    protected Product 상품(String 이름, BigDecimal 가격) {
        Product product = new Product();
        product.setName(이름);
        product.setPrice(가격);
        return productDao.save(product);
    }

    protected MenuProduct 메뉴_상품(Product 상품, long 수량) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(상품.getId());
        menuProduct.setQuantity(수량);
        return menuProduct;
    }
}
