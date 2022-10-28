package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
public abstract class ServiceTestBase {

    @Autowired
    protected OrderDao jdbcTemplateOrderDao;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuDao jdbcTemplateMenuDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    protected ProductRequest createProductRequest(String name, int price) {
        ProductRequest product = new ProductRequest(name, BigDecimal.valueOf(price));

        return product;
    }

    protected MenuRequest createMenuRequest(final String name, final BigDecimal price,
                                            final Long menuGroupId, final List<MenuProductRequest> menuProducts) {
        MenuRequest menu = new MenuRequest(name, price, menuGroupId, menuProducts);

        return menu;
    }

    protected MenuProduct 메뉴_상품_생성(Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(2);

        return orderTable;
    }

    protected OrderTable 빈_주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        return orderTable;
    }

    protected OrderTableCreateRequest createOrderTableCreateRequest(int guest) {
        return new OrderTableCreateRequest(guest, guest == 0);
    }

    protected Order 주문_생성(final OrderTable orderTable) {
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());

        return order;
    }

    protected TableGroup 단체_지정_생성(final OrderTable... orderTables) {
        List<OrderTable> orderTableList = Arrays.stream(orderTables)
                .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTableList);

        return tableGroup;
    }

    protected OrderLineItem 주문_항목_생성(final Order order, final Menu menu, final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        orderLineItem.setOrderId(order.getId());

        return orderLineItem;
    }

    protected Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    protected OrderCreateRequest createOrderCreateRequest(Long orderTableId,
                                                          List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderCreateRequest(orderTableId, orderLineItemRequests);
    }

    protected OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    protected OrderLineItemRequest createOrderLineItemRequest(final Long menuId, final long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
