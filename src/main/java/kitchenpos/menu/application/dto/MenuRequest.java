package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    protected MenuRequest() {
    }

    public MenuRequest(final String name,
                       final BigDecimal price,
                       final Long menuGroupId,
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

    public List<Long> getProductIds() {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    public int getProductSize() {
        return menuProductRequests.size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuRequest that = (MenuRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price)
                && Objects.equals(menuGroupId, that.menuGroupId) && Objects.equals(menuProductRequests,
                that.menuProductRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProductRequests);
    }
}
