package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

    private static final int DEFAULT_MENU_PRODUCT_QUANTITY = 1;
    private static final int DEFAULT_ORDER_LINE_QUANTITY = 1;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    protected Product 토마토파스타;
    protected Product 목살스테이크;
    protected Product 탄산음료;

    protected MenuGroup 파스타;
    protected MenuGroup 스테이크;
    protected MenuGroup 음료;
    protected MenuGroup 세트;

    protected Menu 파스타한상;

    protected OrderTable 빈_테이블1;
    protected OrderTable 빈_테이블2;

    protected OrderTable 손님있는_식사중_테이블;
    protected OrderTable 손님있는_테이블;

    protected Order 식사중인_주문;

    @BeforeEach
    void setUp() {
        토마토파스타 = 상품_등록("토마토파스타", 15000);
        목살스테이크 = 상품_등록("목살스테이크", 20000);
        탄산음료 = 상품_등록("탄산음료", 4000);
        파스타 = 메뉴_그룹_등록("파스타");
        스테이크 = 메뉴_그룹_등록("스테이크");
        음료 = 메뉴_그룹_등록("음료");
        세트 = 메뉴_그룹_등록("세트");
        파스타한상 = 메뉴_등록("파스타한상", 35000, 세트, 토마토파스타, 목살스테이크, 탄산음료);
        빈_테이블1 = 테이블_등록();
        빈_테이블2 = 테이블_등록();
        손님있는_식사중_테이블 = 손님_채운_테이블_생성(3);
        손님있는_테이블 = 손님_채운_테이블_생성(4);
        식사중인_주문 = 주문_요청한다(손님있는_식사중_테이블, 파스타한상);
        식사중인_주문 = 주문을_식사_상태로_만든다(식사중인_주문);
    }

    public Product 상품_등록(final String name, final long price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return productService.create(product);
    }

    public MenuGroup 메뉴_그룹_등록(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupService.create(menuGroup);
    }

    public Menu 메뉴_등록(final String name, final long price, final MenuGroup menuGroup, final Product... products) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(new BigDecimal(price));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(makeMenuProducts(products));
        return menuService.create(menu);
    }

    private List<MenuProduct> makeMenuProducts(final Product[] products) {
        return Arrays.stream(products)
                .map(product -> {
                    final MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setProductId(product.getId());
                    menuProduct.setQuantity(DEFAULT_MENU_PRODUCT_QUANTITY);
                    return menuProduct;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderTable 테이블_등록() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return tableService.create(orderTable);
    }

    public OrderTable 손님_채운_테이블_생성(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable savedTable = tableService.create(orderTable);
        savedTable.setEmpty(false);
        savedTable = tableService.changeEmpty(savedTable.getId(), savedTable);
        savedTable.setNumberOfGuests(numberOfGuests);
        return tableService.changeNumberOfGuests(savedTable.getId(), savedTable);
    }

    public TableGroup 테이블을_그룹으로_묶는다(final OrderTable... tables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(List.of(tables));
        return tableGroupService.create(tableGroup);
    }

    public void 테이블_그룹을_해제한다(final TableGroup tableGroup) {
        tableGroupService.ungroup(tableGroup.getId());
    }

    public Order 주문_요청한다(final OrderTable orderTable, final Menu... menus) {
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(makeOrderLineItems(menus));
        return orderService.create(order);
    }

    private List<OrderLineItem> makeOrderLineItems(final Menu[] menus) {
        return Arrays.stream(menus)
                .map(menu -> {
                    final OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(menu.getId());
                    orderLineItem.setQuantity(DEFAULT_ORDER_LINE_QUANTITY);
                    return orderLineItem;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    public Order 주문을_식사_상태로_만든다(final Order order) {
        order.setOrderStatus(OrderStatus.MEAL.name());
        return orderService.changeOrderStatus(order.getId(), order);
    }

    public Order 주문을_완료_상태로_만든다(final Order order) {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        return orderService.changeOrderStatus(order.getId(), order);
    }
}
