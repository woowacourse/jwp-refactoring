package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MenuRequest {

    @NotEmpty
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long menuGroupId;

    @NotEmpty
    private List<MenuProductRequest> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuRequest that = (MenuRequest) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(menuGroupId, that.menuGroupId) &&
            Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProducts);
    }

    @Override
    public String toString() {
        return "MenuRequest{" +
            "name='" + name + '\'' +
            ", price=" + price +
            ", menuGroupId=" + menuGroupId +
            ", menuProducts=" + menuProducts +
            '}';
    }
}
