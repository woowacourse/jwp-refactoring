package kitchenpos.application.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuResponse that = (MenuResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getMenuGroupId(), that.getMenuGroupId()) && Objects.equals(getMenuProducts(), that.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }

    public Long getId() {
        return id;
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
