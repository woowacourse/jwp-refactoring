package kitchenpos.menu.presentation.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name,
                             final BigDecimal price,
                             final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProducts) {
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException("메뉴그룹 아이디 값은 null이 될 수 없습니다.");
        }
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
