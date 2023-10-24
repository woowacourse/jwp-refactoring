package kitchenpos.application;

import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.request.MenuGroupCreateRequest;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.TableService;
import kitchenpos.application.order.request.TableCreateRequest;
import kitchenpos.application.order.request.TableUpdateRequest;
import kitchenpos.application.product.ProductService;
import kitchenpos.application.product.request.ProductCreateRequest;
import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.application.tablegroup.request.OrderTableIdRequest;
import kitchenpos.application.tablegroup.request.TableGroupCreateRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        에이드 = 메뉴_그룹_등록("에이드");
        프라페 = 메뉴_그룹_등록("프라페");
        세트 = 메뉴_그룹_등록("세트");
        이달의음료세트 = 메뉴_등록("이달의음료세트", 16000L, 세트, 아메리카노, 딸기에이드, 자바칩프라페);
        빈_테이블1 = 테이블_등록();
        빈_테이블2 = 테이블_등록();
        손님있는_식사중_테이블 = 손님_채운_테이블_생성(3);
        손님있는_테이블 = 손님_채운_테이블_생성(4);
        주문 = 주문_요청(손님있는_식사중_테이블, 이달의음료세트);
        주문 = 주문_식사_상태로_변경(주문);
    }

    public Product 상품_등록(String name, Long price) {
        ProductCreateRequest request = new ProductCreateRequest(name, price);
        return productService.create(request);
    }

    public List<Product> 상품_목록_조회() {
        return productService.list();
    }

    public MenuGroup 메뉴_그룹_등록(String name) {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(name);
        return menuGroupService.create(request);
    }

    public List<MenuGroup> 메뉴_그룹_전체_조회() {
        return menuGroupService.list();
    }

    public Menu 메뉴_등록(String name, Long price, MenuGroup menuGroup, Product... products) {
        Menu menu = Menu.of(name, price, menuGroup.getId());
        addMenuProducts(menu, products);
        return menuService.create(menu);
    }

    private void addMenuProducts(Menu menu, Product[] products) {
        Arrays.stream(products)
                .forEach(product -> menu.addProduct(product.getId(), 1));
    }

    public List<Menu> 메뉴_목록_조회() {
        return menuService.list();
    }

    public Menu 메뉴_찾기(Long id) {
        return 메뉴_목록_조회().stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public OrderTable 테이블_등록() {
        TableCreateRequest request = new TableCreateRequest(0, true);
        return tableService.create(request);
    }

    public OrderTable 손님_채운_테이블_생성(int numberOfGuests) {
        TableCreateRequest request = new TableCreateRequest(numberOfGuests, false);
        return tableService.create(request);
    }

    public List<OrderTable> 테이블_목록_조회() {
        return tableService.list();
    }

    public OrderTable 테이블_손님_수_변경(Long orderTableId, int numberOfGuests) {
        TableUpdateRequest updateRequest = new TableUpdateRequest(numberOfGuests, false);
        return tableService.changeNumberOfGuests(orderTableId, updateRequest);
    }

    public OrderTable 테이블_채움(Long orderTableId) {
        TableUpdateRequest updateRequest = new TableUpdateRequest(0, false);
        return tableService.changeEmpty(orderTableId, updateRequest);
    }

    public OrderTable 테이블_비움(Long orderTableId) {
        TableUpdateRequest updateRequest = new TableUpdateRequest(0, true);
        return tableService.changeEmpty(orderTableId, updateRequest);
    }

    public TableUpdateRequest 테이블_비움요청(){
        return new TableUpdateRequest(0, true);
    }

    public TableUpdateRequest 테이블_채움요청(){
        return new TableUpdateRequest(0, false);
    }

    public TableGroup 테이블_그룹화(List<OrderTable> orderTables) {
        List<OrderTableIdRequest> orderTableIdRequests = mapToOrderTableIds(orderTables);
        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIdRequests);
        return tableGroupService.create(request);
    }

    private List<OrderTableIdRequest> mapToOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
    }

    public void 테이블_그룹해제(TableGroup tableGroup) {
        tableGroupService.ungroup(tableGroup.getId());
    }

    public Order 주문_요청(OrderTable orderTable, Menu... menus) {
        Order order = Order.of(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        addOrderLineItems(order, menus);
        return orderService.create(order);
    }

    private void addOrderLineItems(Order order, Menu... menus) {
        makeOrderLineItems(menus)
                .forEach(order::addOrderLineItem);
    }

    private List<OrderLineItem> makeOrderLineItems(Menu[] menus) {
        return Arrays.stream(menus)
                .map(menu -> OrderLineItem.of(null, menu.getId(), 1))
                .collect(Collectors.toUnmodifiableList());
    }

    public Order 주문_식사_상태로_변경(Order order) {
        order.setOrderStatus(OrderStatus.MEAL.name());
        return orderService.changeOrderStatus(order.getId(), order);
    }

    public Order 주문_완료_상태로_변경(Order order) {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        return orderService.changeOrderStatus(order.getId(), order);
    }
}
