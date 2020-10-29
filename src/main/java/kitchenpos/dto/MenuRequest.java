package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuRequest {

    @NotEmpty(message = "이름을 입력해주세요.")
    private final String name;
    @Min(value = 0, message = "이름을 입력해주세요.")
    private final BigDecimal price;
    @NotNull(message = "메뉴그룹을 입력해주세요.")
    private final Long menuGroupId;
    @NotEmpty(message = "메뉴상품을 입력해주세요.")
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
            final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity(final MenuGroup menuGroup) {
        final List<MenuProduct> menuProducts = this.menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());

        return new Menu(name, price, menuGroup, menuProducts);
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
