package kitchenpos.dto.order.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.domain.order.OrderedMenuProduct;

public class OrderedMenuResponse {
    private final long id;
    private final String name;
    private final BigDecimal price;
    private final String menuGroupName;
    private final List<OrderedMenuProductResponse> orderedMenuProducts;

    private OrderedMenuResponse(
            final long id,
            final String name,
            final BigDecimal price,
            final String menuGroupName,
            final List<OrderedMenuProductResponse> orderedMenuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupName = menuGroupName;
        this.orderedMenuProducts = orderedMenuProducts;
    }

    public static OrderedMenuResponse of(
            final OrderedMenu orderedMenu,
            final List<OrderedMenuProduct> orderedMenuProducts
    ) {
        return new OrderedMenuResponse(
                orderedMenu.id(),
                orderedMenu.name(),
                orderedMenu.price().price(),
                orderedMenu.menuGroupName(),
                orderedMenuProducts.stream()
                        .map(OrderedMenuProductResponse::from)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public List<OrderedMenuProductResponse> getOrderedMenuProducts() {
        return orderedMenuProducts;
    }
}
