package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
public abstract class ServiceTestHelper {

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

    protected Product 아메리카노;
    protected Product 딸기에이드;
    protected Product 자바칩프라페;

    protected MenuGroup 커피;
    protected MenuGroup 에이드;
    protected MenuGroup 프라페;
    protected MenuGroup 세트;

    protected Menu 이달의음료세트;

    protected OrderTable 빈_테이블1;
    protected OrderTable 빈_테이블2;

    protected OrderTable 손님있는_식사중_테이블;
    protected OrderTable 손님있는_테이블;

    protected Order 주문;

    @BeforeEach
    void setUp() {
        아메리카노 = 상품_등록("아메리카노", 4500L);
        딸기에이드 = 상품_등록("딸기에이드", 5500L);
        자바칩프라페 = 상품_등록("자바칩프라페", 6000L);
        커피 = 메뉴_그룹_등록("커피");
        에이드 = 메뉴_그룹_등록("스테이크");
        프라페 = 메뉴_그룹_등록("에이드");
        세트 = 메뉴_그룹_등록("세트");
        이달의음료세트 = 메뉴_등록("이달의음료세트", 16000L, 세트, 아메리카노, 딸기에이드, 자바칩프라페);
        빈_테이블1 = 테이블_등록();
        빈_테이블2 = 테이블_등록();
        손님있는_식사중_테이블 = 손님_채운_테이블_생성(3);
        손님있는_테이블 = 손님_채운_테이블_생성(4);
        주문 = 주문_요청(손님있는_식사중_테이블, 이달의음료세트);
        주문 = 주문_식사_상태로_변경(주문);
    }

    public Product 상품_등록(final String name, final Long price) {
        Product product = Product.of(name, createBigDecimal(price));
        return productService.create(product);
    }

    private BigDecimal createBigDecimal(final Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
    }

    public List<Product> 상품_목록_조회() {
        return productService.list();
    }

    public MenuGroup 메뉴_그룹_등록(final String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupService.create(menuGroup);
    }

    public List<MenuGroup> 메뉴_그룹_전체_조회() {
        return menuGroupService.list();
    }

    public Menu 메뉴_등록(final String name, final Long price, final MenuGroup menuGroup, final Product... products) {
        final Menu menu = Menu.of(name, price, menuGroup.getId());
        addMenuProducts(menu, products);
        return menuService.create(menu);
    }

    private void addMenuProducts(final Menu menu, final Product[] products) {
        Arrays.stream(products)
                .forEach(product -> menu.addProduct(product.getId(), 1));
    }

    public List<Menu> 메뉴_목록_조회() {
        return menuService.list();
    }

    public Menu 메뉴_찾기(final Long id) {
        return 메뉴_목록_조회().stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElseThrow();
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

    public List<OrderTable> 테이블_목록_조회() {
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

    public TableGroup 테이블_그룹화(final OrderTable... tables) {
        TableGroup tableGroup = TableGroup.of(List.of(tables));
        return tableGroupService.create(tableGroup);
    }

    public void 테이블_그룹해제(final TableGroup tableGroup) {
        tableGroupService.ungroup(tableGroup.getId());
    }

    public Order 주문_요청(final OrderTable orderTable, final Menu... menus) {
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
                    orderLineItem.setQuantity(1);
                    return orderLineItem;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    public Order 주문_식사_상태로_변경(final Order order) {
        order.setOrderStatus(OrderStatus.MEAL.name());
        return orderService.changeOrderStatus(order.getId(), order);
    }

    public Order 주문_완료_상태로_변경(final Order order) {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        return orderService.changeOrderStatus(order.getId(), order);
    }
}
