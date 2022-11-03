package kitchenpos.support;

import static kitchenpos.fixture.MenuTestFixture.떡볶이;
import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class ServiceTestBase {

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
    protected ProductDao productDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    protected Product 상품_생성(final ProductFixture productFixture) {
        return productDao.save(productFixture.toEntity());
    }

    protected List<MenuGroup> 메뉴_그룹_목록_생성() {
        return Arrays.stream(MenuGroupFixture.values())
                .map(it -> menuGroupDao.save(it.toEntity()))
                .collect(Collectors.toList());
    }

    protected Menu 분식_메뉴_생성() {
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록(product);
        Menu menu = 떡볶이.toEntity(menuGroup.getId(), menuProducts);
        return menuDao.save(menu);
    }

    protected List<MenuProduct> 메뉴_상품_목록(final Product... products) {
        return 메뉴_상품_목록(3, products);
    }

    protected List<MenuProduct> 메뉴_상품_목록(final long quantity, final Product... products) {
        return Arrays.stream(products)
                .map(it -> 메뉴_상품(it, quantity))
                .collect(Collectors.toList());
    }

    protected MenuProduct 메뉴_상품(final Product product, final long quantity) {
        return new MenuProduct(null, null, product.getId(), quantity);
    }

    protected void 주문_생성(final Menu menu, final OrderTable orderTable, final OrderStatus orderStatus) {
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목(menu.getId()));
        Order order = new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);
        orderDao.save(order);
    }

    protected OrderLineItem 주문_항목(final Long menuId) {
        return new OrderLineItem(null, null, menuId, 1);
    }

    protected Order 주문(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    protected TableGroup 단체_지정_생성() {
        return 단체_지정_생성(new ArrayList<>());
    }

    protected TableGroup 단체_지정_생성(final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());
        tableGroup.addOrderTables(orderTables);

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        updateOrderTables(savedTableGroup, orderTables);
        return savedTableGroup;
    }

    private void updateOrderTables(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(tableGroup.getId());
            orderTableDao.save(orderTable);
        }
    }

    protected OrderTable 주문_테이블_생성() {
        return 주문_테이블_생성(null);
    }

    protected OrderTable 주문_테이블_생성(final Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.changeNumberOfGuests(3);
        return orderTableDao.save(orderTable);
    }

    protected OrderTable 빈_주문_테이블_생성() {
        OrderTable orderTable = new OrderTable(0, true);
        return orderTableDao.save(orderTable);
    }
}
