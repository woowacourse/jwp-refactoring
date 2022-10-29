package kitchenpos.application.response;

import kitchenpos.application.response.menu.MenuProductResponse;
import kitchenpos.application.response.menu.MenuResponse;
import kitchenpos.application.response.menugroup.MenuGroupResponse;
import kitchenpos.application.response.order.OrderLineItemResponse;
import kitchenpos.application.response.order.OrderResponse;
import kitchenpos.application.response.product.ProductResponse;
import kitchenpos.application.response.tablegroup.OrderTableResponse;
import kitchenpos.application.response.tablegroup.TableGroupResponse;
import kitchenpos.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseAssembler {

    public List<OrderResponse> orderResponses(final List<Order> orders) {
        return orders.stream()
                .map(this::orderResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderResponse orderResponse(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponses(order.getOrderLineItems())
        );
    }

    private List<OrderLineItemResponse> orderLineItemResponses(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(
                        orderLineItem.getSeq(),
                        orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<MenuResponse> menuResponses(final List<Menu> menus) {
        return menus.stream()
                .map(this::menuResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public MenuResponse menuResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductResponses(menu.getMenuProducts())
        );
    }

    private List<MenuProductResponse> menuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProductResponse(
                        menuProduct.getSeq(),
                        menuProduct.getMenuId(),
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<MenuGroupResponse> menuGroupResponses(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(this::menuGroupResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public MenuGroupResponse menuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }

    public List<ProductResponse> productResponses(final List<Product> products) {
        return products.stream()
                .map(this::productResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public ProductResponse productResponse(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

    public TableGroupResponse tableGroupResponse(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTableResponses(tableGroup.getOrderTables())
        );
    }

    public List<OrderTableResponse> orderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> new OrderTableResponse(
                        orderTable.getId(),
                        orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(),
                        orderTable.isEmpty()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderTableResponse orderTableResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }
}
