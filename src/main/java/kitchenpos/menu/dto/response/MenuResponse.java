package kitchenpos.menu.dto.response;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProductResponses;

    public static MenuResponse of(Menu menu, List<MenuProduct> menuProducts) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroup().getId(),
                MenuProductResponse.toList(menuProducts)
        );
    }

    protected MenuResponse() {
    }

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
                         List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponses;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    public void setMenuProductResponses(final List<MenuProductResponse> menuProductResponses) {
        this.menuProductResponses = menuProductResponses;
    }
}
