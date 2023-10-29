package kitchenpos.dto;

import kitchenpos.domain.menu.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CreateMenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<CreateMenuProductRequest> menuProducts;

    public CreateMenuRequest() {
    }

    public CreateMenuRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<CreateMenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> toMenuProducts() {
        return menuProducts.stream()
                           .map(CreateMenuProductRequest::toMenuProduct)
                           .collect(Collectors.toList());
    }
}
