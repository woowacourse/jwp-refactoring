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
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
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

    @Autowired
    protected MenuRepository menuRepository;

    protected Product 토마토파스타;
    protected Product 목살스테이크;
    protected Product 탄산음료;

    protected MenuGroup 파스타;
    protected MenuGroup 스테이크;
    protected MenuGroup 음료;
    protected MenuGroup 세트;

    protected MenuDto 파스타한상;

    protected TableDto 빈_테이블1;
    protected TableDto 빈_테이블2;

    protected TableDto 손님있는_식사중_테이블;
    protected TableDto 손님있는_테이블;

    protected OrderDto 식사중인_주문;

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

    public MenuDto 메뉴_등록(final String name, final Long price, final MenuGroup menuGroup, final Long... productIds) {
        final List<MenuProduct> menuProducts = Arrays.stream(productIds)
                .map(MenuProduct::new)
                .collect(Collectors.toList());
        final Menu menu = Menu.create(name, createBigDecimalPrice(price), menuGroup, menuProducts);
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
        return menuService.create(createMenuDto);
    }

    private BigDecimal createBigDecimalPrice(final Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
    }

    public List<MenuDto> 메뉴_전체_조회() {
        return menuService.list();
    }

    public MenuDto 메뉴_찾기(final Long id) {
        return 메뉴_전체_조회().stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public TableDto 테이블_등록() {
        final OrderTable orderTable = OrderTable.create();
        return tableService.create(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public TableDto 손님_채운_테이블_생성(final int numberOfGuests) {
        return tableService.create(numberOfGuests, false);
    }

    public List<TableDto> 테이블_전체_조회() {
        return tableService.list();
    }

    public TableDto 테이블_손님_수_변경(final Long orderTableId, final int numberOfGuests) {
        return tableService.changeNumberOfGuests(orderTableId, numberOfGuests);
    }

    public TableDto 테이블_채움(final Long orderTableId) {
        return tableService.changeEmpty(orderTableId, false);
    }

    public TableDto 테이블_비움(final Long orderTableId) {
        return tableService.changeEmpty(orderTableId, true);
    }

    public TableGroupDto 테이블을_그룹으로_묶는다(final TableDto... tables) {
        final List<Long> tableIds = Stream.of(tables)
                .map(TableDto::getId)
                .collect(Collectors.toList());
        return tableGroupService.create(tableIds);
    }

    public void 테이블_그룹을_해제한다(final TableGroupDto tableGroup) {
        tableGroupService.ungroup(tableGroup.getId());
    }

    public OrderDto 주문_요청한다(final TableDto orderTable, final Long... menuIds) {
        final List<OrderLineItem> orderLineItems = Arrays.stream(menuIds)
                .map(menuId -> new OrderLineItem(menuId, 1))
                .collect(Collectors.toList());
        final Order order = Order.create(orderTable.getId(), orderLineItems);
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
        return orderService.create(createOrderDto);
    }

    public OrderDto 주문을_식사_상태로_만든다(final OrderDto order) {
        return orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL);
    }

    public OrderDto 주문을_완료_상태로_만든다(final OrderDto order) {
        return orderService.changeOrderStatus(order.getId(), OrderStatus.COMPLETION);
    }
}
