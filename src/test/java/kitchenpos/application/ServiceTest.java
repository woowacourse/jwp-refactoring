package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.CreateMenuProductDto;
import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.CreateOrderLineItemDto;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.application.dto.TableDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
public abstract class ServiceTest {

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
        파스타한상 = 메뉴_등록("파스타한상", 35000L, 세트, 토마토파스타.getId(), 목살스테이크.getId(), 탄산음료.getId());
        빈_테이블1 = 테이블_등록();
        빈_테이블2 = 테이블_등록();
        손님있는_식사중_테이블 = 손님_채운_테이블_생성(3);
        손님있는_테이블 = 손님_채운_테이블_생성(4);
        식사중인_주문 = 주문_요청한다(손님있는_식사중_테이블, 파스타한상.getId());
        식사중인_주문 = 주문을_식사_상태로_만든다(식사중인_주문);
    }

    public Product 상품_등록(final String name, final Long price) {
        final ProductDto productDto = productService.create(name, createBigDecimalPrice(price));
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getPrice()
        );
    }

    public List<Product> 상품_전체_조회() {
        return productService.list()
                .stream()
                .map(productDto ->
                        new Product(
                                productDto.getId(),
                                productDto.getName(),
                                productDto.getPrice()
                        )
                )
                .collect(Collectors.toList());
    }

    public MenuGroup 메뉴_그룹_등록(final String name) {
        final MenuGroupDto menuGroupDto = menuGroupService.create(name);
        return new MenuGroup(
                menuGroupDto.getId(),
                menuGroupDto.getName()
        );
    }

    public List<MenuGroup> 메뉴_그룹_전체_조회() {
        return menuGroupService.list()
                .stream()
                .map(menuGroupDto ->
                        new MenuGroup(
                                menuGroupDto.getId(),
                                menuGroupDto.getName()
                        )
                )
                .collect(Collectors.toList());
    }

    public Menu 메뉴_등록(final String name, final Long price, final MenuGroup menuGroup, final Long... productIds) {
        final List<MenuProduct> menuProducts = Arrays.stream(productIds)
                .map(MenuProduct::new)
                .collect(Collectors.toList());
        final Menu menu = Menu.create(name, createBigDecimalPrice(price), menuGroup.getId(), menuProducts);
        final List<CreateMenuProductDto> createMenuProductDtos = menu.getMenuProducts()
                .stream()
                .map(menuProduct ->
                        new CreateMenuProductDto(menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
        final CreateMenuDto createMenuDto = new CreateMenuDto(
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                createMenuProductDtos
        );
        final MenuDto menuDto = menuService.create(createMenuDto);

        return makeMenu(menuDto);
    }

    private BigDecimal createBigDecimalPrice(final Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
    }

    public List<Menu> 메뉴_전체_조회() {
        return menuService.list()
                .stream()
                .map(this::makeMenu)
                .collect(Collectors.toList());
    }

    private Menu makeMenu(final MenuDto menuDto) {
        final Menu menu = new Menu(
                menuDto.getId(),
                menuDto.getName(),
                Price.valueOf(menuDto.getPrice()),
                menuDto.getMenuGroupId()
        );
        menuDto.getMenuProducts().forEach(menuProductDto ->
                menu.addProduct(menuProductDto.getProductId(), menuProductDto.getQuantity())
        );
        return menu;
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
        final OrderTable orderTable = OrderTable.create();
        final TableDto tableDto = tableService.create(orderTable.getNumberOfGuests(), orderTable.isEmpty());

        return new OrderTable(
                tableDto.getId(),
                tableDto.getTableGroupId(),
                tableDto.getNumberOfGuests(),
                tableDto.getEmpty()
        );
    }

    public OrderTable 손님_채운_테이블_생성(final int numberOfGuests) {
        final TableDto tableDto = tableService.create(numberOfGuests, false);
        return new OrderTable(
                tableDto.getId(),
                tableDto.getTableGroupId(),
                tableDto.getNumberOfGuests(),
                tableDto.getEmpty()
        );
    }

    public List<OrderTable> 테이블_전체_조회() {
        return tableService.list()
                .stream()
                .map(tableDto ->
                        new OrderTable(
                                tableDto.getId(),
                                tableDto.getTableGroupId(),
                                tableDto.getNumberOfGuests(),
                                tableDto.getEmpty()
                        )
                )
                .collect(Collectors.toList());
    }

    public OrderTable 테이블_손님_수_변경(final Long orderTableId, final int numberOfGuests) {
        final TableDto tableDto = tableService.changeNumberOfGuests(orderTableId, numberOfGuests);
        return new OrderTable(
                tableDto.getId(),
                tableDto.getTableGroupId(),
                tableDto.getNumberOfGuests(),
                tableDto.getEmpty()
        );
    }

    public OrderTable 테이블_채움(final Long orderTableId) {
        final TableDto tableDto = tableService.changeEmpty(orderTableId, false);
        return new OrderTable(
                tableDto.getId(),
                tableDto.getTableGroupId(),
                tableDto.getNumberOfGuests(),
                tableDto.getEmpty()
        );
    }

    public OrderTable 테이블_비움(final Long orderTableId) {
        final TableDto tableDto = tableService.changeEmpty(orderTableId, true);
        return new OrderTable(
                tableDto.getId(),
                tableDto.getTableGroupId(),
                tableDto.getNumberOfGuests(),
                tableDto.getEmpty()
        );
    }

    public TableGroup 테이블을_그룹으로_묶는다(final OrderTable... tables) {
        final List<Long> tableIds = Stream.of(tables)
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final TableGroupDto tableGroupDto = tableGroupService.create(tableIds);
        final List<OrderTable> orderTables = tableGroupDto.getOrderTables()
                .stream().map(tableDto ->
                        new OrderTable(
                                tableDto.getId(),
                                tableDto.getTableGroupId(),
                                tableDto.getNumberOfGuests(),
                                tableDto.getEmpty()
                        )
                ).collect(Collectors.toList());
        return new TableGroup(
                tableGroupDto.getId(),
                tableGroupDto.getCreatedDate(),
                orderTables
        );
    }

    public void 테이블_그룹을_해제한다(final TableGroup tableGroup) {
        tableGroupService.ungroup(tableGroup.getId());
    }

    public Order 주문_요청한다(final OrderTable orderTable, final Long... menuIds) {
        final Order order = Order.create(orderTable.getId(), List.of(menuIds));
        final List<CreateOrderLineItemDto> createOrderLineItemDtos = order.getOrderLineItems()
                .stream()
                .map(orderLineItem ->
                        new CreateOrderLineItemDto(
                                orderLineItem.getMenuId(),
                                orderLineItem.getQuantity()
                        )
                )
                .collect(Collectors.toList());
        final CreateOrderDto createOrderDto = new CreateOrderDto(
                orderTable.getId(),
                createOrderLineItemDtos
        );
        final OrderDto orderDto = orderService.create(createOrderDto);
        final Order result = new Order(
                orderDto.getId(),
                orderDto.getOrderTableId(),
                orderDto.getOrderStatus(),
                orderDto.getOrderedTime()
        );

        createOrderLineItemDtos.forEach(createOrderLineItemDto ->
                result.addMenu(createOrderLineItemDto.getMenuId(), createOrderLineItemDto.getQuantity())
        );

        return result;
    }

    public List<Order> 모든_주문_조회() {
        return orderService.list()
                .stream()
                .map(orderDto ->
                        new Order(
                                orderDto.getId(),
                                orderDto.getOrderTableId(),
                                orderDto.getOrderStatus(),
                                orderDto.getOrderedTime()
                        )
                )
                .collect(Collectors.toList());
    }

    public Order 주문을_식사_상태로_만든다(final Order order) {
        final OrderDto orderDto = orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL);
        return new Order(
                orderDto.getId(),
                orderDto.getOrderTableId(),
                orderDto.getOrderStatus(),
                orderDto.getOrderedTime()
        );
    }

    public Order 주문을_완료_상태로_만든다(final Order order) {
        final OrderDto orderDto = orderService.changeOrderStatus(order.getId(), OrderStatus.COMPLETION);
        return new Order(
                orderDto.getId(),
                orderDto.getOrderTableId(),
                orderDto.getOrderStatus(),
                orderDto.getOrderedTime()
        );
    }
}
