package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.TableGroup;
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

    @Autowired
    protected MenuProductRepository menuProductRepository;

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
        토마토파스타 = 상품_등록("토마토파스타", 15000L);
        목살스테이크 = 상품_등록("목살스테이크", 20000L);
        탄산음료 = 상품_등록("탄산음료", 4000L);
        파스타 = 메뉴_그룹_등록("파스타");
        스테이크 = 메뉴_그룹_등록("스테이크");
        음료 = 메뉴_그룹_등록("음료");
        세트 = 메뉴_그룹_등록("세트");
        파스타한상 = 메뉴_등록("파스타한상", 35000L, 세트, 토마토파스타, 목살스테이크, 탄산음료);
        빈_테이블1 = 테이블_등록();
        빈_테이블2 = 테이블_등록();
        손님있는_식사중_테이블 = 손님_채운_테이블_생성(3);
        손님있는_테이블 = 손님_채운_테이블_생성(4);
        식사중인_주문 = 주문_요청한다(손님있는_식사중_테이블, 파스타한상);
        식사중인_주문 = 주문을_식사_상태로_만든다(식사중인_주문);
    }

    public Product 상품_등록(final String name, final Long price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(createBigDecimal(price));
        return productService.create(product);
    }

    public List<Product> 상품_전체_조회() {
        return productService.list();
    }

    public MenuGroup 메뉴_그룹_등록(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupService.create(menuGroup);
    }

    public List<MenuGroup> 메뉴_그룹_전체_조회() {
        return menuGroupService.list();
    }

    public Menu 메뉴_등록(final String name, final Long price, final MenuGroup menuGroup, final Product... products) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(createBigDecimal(price));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(makeMenuProducts(products));
        return menuService.create(menu);
    }

    private BigDecimal createBigDecimal(final Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
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

    public List<Menu> 메뉴_전체_조회() {
        return menuService.list();
    }

    public Menu 메뉴_찾기(final Long id) {
        return 메뉴_전체_조회().stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public List<MenuProduct> 메뉴_상품_조회(final Menu menu) {
        return menuProductRepository.findAllByMenuId(menu.getId());
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

    public List<OrderTable> 테이블_전체_조회() {
        return tableService.list();
    }

    public OrderTable 테이블_손님_수_변경(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return tableService.changeNumberOfGuests(orderTableId, orderTable);
    }

    public OrderTable 테이블_채움(final Long orderTableId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        orderTable.setEmpty(false);
        return tableService.changeEmpty(orderTableId, orderTable);
    }

    public OrderTable 테이블_비움(final Long orderTableId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        orderTable.setEmpty(true);
        return tableService.changeEmpty(orderTableId, orderTable);
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

    public List<Order> 모든_주문_조회() {
        return orderService.list();
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
