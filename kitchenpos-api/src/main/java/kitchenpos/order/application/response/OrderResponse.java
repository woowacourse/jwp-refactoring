package kitchenpos.order.application.response;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.table.domain.OrderTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTable(), order.getOrderStatus(), order.getOrderedTime(),
                order.getOrderLineItems().stream().map(OrderLineItemResponse::from).collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemResponse {
        private Long seq;
        private Long orderId;
        private OrderedMenuResponse orderedMenuResponse;
        private long quantity;

        private OrderLineItemResponse(Long seq, Long orderId, OrderedMenuResponse orderedMenuResponse, long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.orderedMenuResponse = orderedMenuResponse;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
            return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrder().getId(),
                    OrderedMenuResponse.from(orderLineItem.getOrderedMenu()), orderLineItem.getQuantity());
        }

        public Long getSeq() {
            return seq;
        }

        public Long getOrderId() {
            return orderId;
        }

        public OrderedMenuResponse getOrderedMenuResponse() {
            return orderedMenuResponse;
        }

        public long getQuantity() {
            return quantity;
        }

        public static class OrderedMenuResponse {
            private Long menuId;
            private String menuName;
            private BigDecimal menuPrice;

            private OrderedMenuResponse(Long menuId, String menuName, BigDecimal menuPrice) {
                this.menuId = menuId;
                this.menuName = menuName;
                this.menuPrice = menuPrice;
            }

            public static OrderedMenuResponse from(OrderedMenu orderedMenu) {
                return new OrderedMenuResponse(orderedMenu.getMenuId(), orderedMenu.getMenuName(), orderedMenu.getMenuPrice());
            }

            public Long getMenuId() {
                return menuId;
            }

            public String getMenuName() {
                return menuName;
            }

            public BigDecimal getMenuPrice() {
                return menuPrice;
            }
        }
    }
}
