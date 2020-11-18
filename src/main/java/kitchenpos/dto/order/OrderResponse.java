package kitchenpos.dto.order;

import kitchenpos.domain.order.Order;
import kitchenpos.dto.menu.MenuResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTableResponse;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<MenuResponse> menus;

    public OrderResponse(Order order, List<MenuResponse> menus) {
        this.id = order.getId();
        this.orderTableResponse = new OrderTableResponse(order.getOrderTable());
        this.orderStatus = order.getOrderStatus().toString();
        this.orderedTime = order.getOrderedTime();
        this.menus = menus;
    }
}
