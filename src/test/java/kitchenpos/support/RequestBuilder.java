package kitchenpos.support;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.dto.request.IdRequest;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.request.TableGroupRequest;

public class RequestBuilder {

    private static final String DEFAULT_PRODUCT_NAME = "콜라";
    private static final Integer DEFAULT_PRODUCT_PRICE = 2000;
    private static final String DEFAULT_MENU_GROUP_NAME = "음료 메뉴";
    private static final String DEFAULT_MENU_NAME = "포키 정식";

    public static ProductRequest ofProduct() {
        return ofProduct(DEFAULT_PRODUCT_PRICE);
    }

    public static ProductRequest ofProduct(final int price) {
        return ofProduct(new BigDecimal(price));
    }

    public static ProductRequest ofProduct(final BigDecimal price) {
        return new ProductRequest(DEFAULT_PRODUCT_NAME, price);
    }

    public static MenuGroupRequest ofMenuGroup() {
        return new MenuGroupRequest(DEFAULT_MENU_GROUP_NAME);
    }

    public static MenuRequest ofMenu(final MenuGroup menuGroup, final List<MenuProduct> menuProducts, final int price) {
        return ofMenu(menuGroup, menuProducts, new BigDecimal(price));
    }

    public static MenuRequest ofMenu(final MenuGroup menuGroup,
                                     final List<MenuProduct> menuProducts,
                                     final BigDecimal price) {
        final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(menuProduct -> new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());

        return new MenuRequest(DEFAULT_MENU_NAME, price, menuGroup.getId(), menuProductRequests);
    }

    public static OrderTableRequest ofEmptyTable() {
        return new OrderTableRequest(0, true);
    }

    public static OrderTableRequest ofFullTable() {
        return ofTableWithGuests(2);
    }

    public static OrderTableRequest ofTableWithGuests(final int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests, false);
    }

    public static TableGroupRequest ofTableGroup(final OrderTable... orderTables) {
        final List<IdRequest> orderTableIds = Arrays.stream(orderTables)
                .map(OrderTable::getId)
                .map(IdRequest::new)
                .collect(Collectors.toList());

        return new TableGroupRequest(orderTableIds);
    }

    public static OrderStatusRequest ofOrder(final OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }

    public static OrderRequest ofOrder(final Menu menu, final OrderTable orderTable) {
        return ofOrder(menu.getId(), orderTable.getId());
    }

    public static OrderRequest ofOrderWithoutMenu(final OrderTable orderTable) {
        return ofOrder(null, orderTable.getId());
    }

    private static OrderRequest ofOrder(final Long menuId, final Long tableId) {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, 1);
        return new OrderRequest(tableId, Arrays.asList(orderLineItemRequest));
    }
}
