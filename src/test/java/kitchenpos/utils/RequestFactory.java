package kitchenpos.utils;

import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderTableIdRequest;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class RequestFactory {
    public static MenuRequest CREATE_MENU_REQUEST(String name,
                                                  BigDecimal price,
                                                  Long menuGroupId,
                                                  List<MenuProductRequest> menuProductRequests
    ) {
        return MenuRequest.of(name, price, menuGroupId, menuProductRequests);
    }

    public static MenuProductRequest CREATE_MENU_PRODUCT_REQUEST(Long productId, Long quantity) {
        return MenuProductRequest.from(productId, quantity);
    }

    public static OrderRequest CREATE_ORDER_REQUEST(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return OrderRequest.of(orderTableId, orderLineItemRequests);
    }

    public static OrderLineItemRequest CREATE_ORDER_LINE_ITEM_REQUEST(Long menuId, Long quantity) {
        return OrderLineItemRequest.of(menuId, quantity);
    }

    public static OrderStatusRequest CREATE_ORDER_STATUS_REQUEST(String orderStatus) {
        return OrderStatusRequest.from(orderStatus);
    }

    public static ProductRequest CREATE_PRODUCT_REQUEST(String name, BigDecimal price) {
        return ProductRequest.from(name, price);
    }

    public static TableGroupRequest CREATE_TABLE_GROUP_REQUEST(List<Long> orderTableIds) {
        List<OrderTableIdRequest> orderTableIdRequests = orderTableIds.stream()
                .map(OrderTableIdRequest::from)
                .collect(Collectors.toList());
        return TableGroupRequest.from(orderTableIdRequests);
    }

    public static TableRequest CREATE_TABLE_REQUEST(int numberOfGuests, boolean empty) {
        return TableRequest.of(numberOfGuests, empty);
    }
}
