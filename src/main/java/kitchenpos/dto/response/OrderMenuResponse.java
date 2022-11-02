package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderMenu;

public class OrderMenuResponse {

    private Long id;
    private String menuName;
    private BigDecimal menuPrice;
    private String menuGroupName;
    private List<OrderMenuProductResponse> orderMenuProducts;

    public OrderMenuResponse(final Long id, final String menuName, final BigDecimal menuPrice,
                             final String menuGroupName,
                             final List<OrderMenuProductResponse> orderMenuProducts) {
        this.id = id;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuGroupName = menuGroupName;
        this.orderMenuProducts = orderMenuProducts;
    }

    public static OrderMenuResponse from(final OrderMenu orderMenu) {
        final List<OrderMenuProductResponse> orderMenuProductResponses = orderMenu.getOrderMenuProducts()
                .stream()
                .map(OrderMenuProductResponse::from)
                .collect(Collectors.toList());
        return new OrderMenuResponse(orderMenu.getId(), orderMenu.getMenuName(), orderMenu.getMenuPrice(),
                orderMenu.getMenuGroupName(), orderMenuProductResponses);
    }

    public Long getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public List<OrderMenuProductResponse> getOrderMenuProducts() {
        return orderMenuProducts;
    }
}
