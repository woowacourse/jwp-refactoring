package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

    @NotBlank
    private final String name;

    @PositiveOrZero
    private final BigDecimal price;

    @NotNull
    private final Long menuGroupId;

    @NotNull
    @NotEmpty
    private final List<@Valid MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return new Menu(this.name, this.price, this.menuGroupId);
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
            .map(MenuProductCreateRequest::getProductId)
            .collect(Collectors.toList());
    }
}
