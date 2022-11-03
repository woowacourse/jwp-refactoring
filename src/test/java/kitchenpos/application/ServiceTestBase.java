package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.ui.dto.request.MenuGroupRequest;
import kitchenpos.menu.ui.dto.request.MenuProductRequest;
import kitchenpos.menu.ui.dto.request.MenuRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.dto.request.OrderCreateRequest;
import kitchenpos.order.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ordertable.ui.dto.request.OrderTableEmptyStatusRequest;
import kitchenpos.ordertable.ui.dto.request.OrderTableGuestRequest;
import kitchenpos.ordertable.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ordertable.ui.dto.request.TableGroupOrderTableIdRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.ui.dto.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
public abstract class ServiceTestBase {

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    protected ProductRequest createProductRequest(String name, int price) {
        return new ProductRequest(name, BigDecimal.valueOf(price));
    }

    protected Product createProduct(final String name, final BigDecimal price) {
        return new Product(name, price);
    }

    protected MenuRequest createMenuRequest(final String name, final BigDecimal price,
                                            final Long menuGroupId, final List<MenuProductRequest> menuProducts) {

        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    protected Menu createMenu(final String name, final BigDecimal price,
                              final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    protected MenuProduct createMenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        return new MenuProduct(productId, quantity, price);
    }

    protected MenuGroupRequest createMenuGroupRequest(final String name) {
        return new MenuGroupRequest(name);
    }

    protected OrderTable 주문_테이블_생성() {
        return new OrderTable(2, false);
    }

    protected OrderTable 빈_주문_테이블_생성() {
        return new OrderTable(0, true);
    }

    protected OrderTableCreateRequest createOrderTableCreateRequest(final int guest) {
        return new OrderTableCreateRequest(guest, guest == 0);
    }

    protected OrderTableEmptyStatusRequest createOrderTableEmptyStatusRequest(final boolean empty) {
        return new OrderTableEmptyStatusRequest(empty);
    }

    protected OrderTableGuestRequest createOrderTableGuestRequest(final int guest) {
        return new OrderTableGuestRequest(guest);
    }

    protected TableGroup 단체_지정_생성(final OrderTable... orderTables) {
        List<OrderTable> orderTableList = Arrays.stream(orderTables)
                .collect(Collectors.toList());

        return new TableGroup(LocalDateTime.now(), orderTableList);
    }

    protected TableGroupCreateRequest createTableGroupCreateRequest(final OrderTable... orderTables) {
        List<TableGroupOrderTableIdRequest> orderTableIds = Arrays.stream(orderTables)
                .map(orderTable -> new TableGroupOrderTableIdRequest(orderTable.getId()))
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(orderTableIds);
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


    protected Order 주문_생성_및_저장(final OrderTable orderTable, final Menu menu, final long quantity) {
        List<OrderLineItem> orderLineItems = Collections.singletonList(createOrderLineItem(menu.getId(), quantity));
        Order order = createOrder(orderTable.getId(), orderLineItems);

        return orderRepository.save(order);
    }
}
