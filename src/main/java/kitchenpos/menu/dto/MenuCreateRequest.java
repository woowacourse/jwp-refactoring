package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuCreateRequest(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductRequest> menuProducts
    ) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("가격은 빈 값일 수 없습니다.");
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
