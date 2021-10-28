package kitchenpos.application;

import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.OrderItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderTableIdRequest;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableRequest;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ActiveProfiles("test")
@MockitoSettings
class ServiceTest {
    protected static MenuRequest CREATE_MENU_REQUEST(String name,
                                                     BigDecimal price,
                                                     Long menuGroupId,
                                                     List<MenuProductRequest> menuProductRequests
    ) {
        return MenuRequest.of(name, price, menuGroupId, menuProductRequests);
    }


    protected static MenuGroupRequest CREATE_MENU_GROUP_REQUEST(String name) {
        return MenuGroupRequest.from(name);
    }

    protected static MenuProductRequest CREATE_MENU_PRODUCT_REQUEST(Long productId, Long quantity) {
        return MenuProductRequest.from(productId, quantity);
    }

    protected static OrderRequest CREATE_ORDER_REQUEST(Long orderTableId, List<OrderItemRequest> orderItemRequests) {
        return OrderRequest.of(orderTableId, orderItemRequests);
    }

    protected static OrderItemRequest CREATE_ORDER_ITEM_REQUEST(Long menuId, Long quantity) {
        return OrderItemRequest.of(menuId, quantity);
    }

    protected static OrderStatusRequest CREATE_ORDER_STATUS_REQUEST(String orderStatus) {
        return OrderStatusRequest.from(orderStatus);
    }

    protected static ProductRequest CREATE_PRODUCT_REQUEST(String name, BigDecimal price) {
        return ProductRequest.from(name, price);
    }

    protected static TableGroupRequest CREATE_TABLE_GROUP_REQUEST(List<Long> orderTableIds) {
        List<OrderTableIdRequest> orderTableIdRequests = orderTableIds.stream()
                .map(OrderTableIdRequest::from)
                .collect(Collectors.toList());
        return TableGroupRequest.from(orderTableIdRequests);
    }

    protected static TableRequest CREATE_TABLE_REQUEST(int numberOfGuests, boolean empty) {
        return TableRequest.of(numberOfGuests, empty);
    }
}
