package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequestDto {

    private final String name;

    private final BigDecimal price;

    private final Long menuGroupId;

    private final List<MenuProduct> menuProducts;

    public MenuCreateRequestDto(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        validatePrice(price);
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0이거나 음수일 수 없습니다.");
        }
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
