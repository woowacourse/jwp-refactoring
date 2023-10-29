package kitchenpos.api.menu.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.core.menu.application.dto.MenuProductRequest;

public class MenuRequest {
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    @JsonProperty("menuProducts")
    private final List<MenuProductRequest> menuProductRequests;

    public MenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
                       final List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuRequest that = (MenuRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price)
                && Objects.equals(menuGroupId, that.menuGroupId) && Objects.equals(menuProductRequests,
                that.menuProductRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProductRequests);
    }
}
