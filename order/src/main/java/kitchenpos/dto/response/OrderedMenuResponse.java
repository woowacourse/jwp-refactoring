package kitchenpos.dto.response;

import kitchenpos.domain.OrderedMenu;
import java.math.BigDecimal;

public class OrderedMenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private String orderedMenuGroup;

    public OrderedMenuResponse() {
    }

    private OrderedMenuResponse(
            Long id,
            String name,
            BigDecimal price,
            String orderedMenuGroup
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderedMenuGroup = orderedMenuGroup;
    }

    public static OrderedMenuResponse from(OrderedMenu orderedMenu) {
        return new OrderedMenuResponse(
                orderedMenu.getId(),
                orderedMenu.getName(),
                orderedMenu.getPrice().getValue(),
                orderedMenu.getOrderedMenuGroupName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getOrderedMenuGroup() {
        return orderedMenuGroup;
    }
}
