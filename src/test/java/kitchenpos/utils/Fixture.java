package kitchenpos.utils;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.dto.MenuGroupRequest;
import kitchenpos.menu.ui.dto.MenuProductRequest;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.ui.dto.OrderLineItemRequest;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.order.ui.dto.OrderStatusRequest;
import kitchenpos.order.ui.dto.OrderTableIdRequest;
import kitchenpos.order.ui.dto.TableGroupRequest;
import kitchenpos.order.ui.dto.TableRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.dto.ProductRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Fixture {
    public static class RequestFactory {
        public static MenuRequest CREATE_MENU_REQUEST(String name,
                                                         BigDecimal price,
                                                         Long menuGroupId,
                                                         List<MenuProductRequest> menuProductRequests
        ) {
            return MenuRequest.of(name, price, menuGroupId, menuProductRequests);
        }

        public static MenuUpdateRequest CREATE_MENU_UPDATE_REQUEST(String name, BigDecimal price) {
            return MenuUpdateRequest.of(name, price);
        }

        public static MenuGroupRequest CREATE_MENU_GROUP_REQUEST(String name) {
            return MenuGroupRequest.from(name);
        }

        public static MenuProductRequest CREATE_MENU_PRODUCT_REQUEST(Long productId, Long quantity) {
            return MenuProductRequest.from(productId, quantity);
        }

        public static OrderRequest CREATE_ORDER_REQUEST(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
            return OrderRequest.of(orderTableId, orderLineItemRequests);
        }

        public static OrderLineItemRequest CREATE_ORDER_ITEM_REQUEST(Long menuId, Long quantity) {
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

    public static class DomainFactory {
        public static Menu CREATE_MENU(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
            return new Menu(id, name, price, menuGroup);
        }

        public static MenuGroup CREATE_MENU_GROUP(Long id, String name) {
            return new MenuGroup(id, name);
        }

        public static Order CREATE_ORDER(Long id, OrderTable orderTable, String orderStatus) {
            return new Order(id, orderTable, orderStatus, LocalDateTime.now());
        }

        public static OrderLineItem CREATE_ORDER_LINE_ITEM(Long seq, Order order, Menu menu, Long quantity) {
            return new OrderLineItem(seq, order, menu, quantity);
        }

        public static OrderTable CREATE_ORDER_TABLE(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
            return new OrderTable(id, tableGroup, numberOfGuests, empty);
        }

        public static TableGroup CREATE_TABLE_GROUP(Long id) {
            return new TableGroup(id, LocalDateTime.now());
        }

        public static Product CREATE_PRODUCT(Long id, String name, BigDecimal price) {
            return new Product(id, name, price);
        }
    }
}
