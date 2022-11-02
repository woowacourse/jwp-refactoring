package kitchenpos.menu.presentation.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class MenuRequest {

    private Long id;

    private String name;

    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

    public MenuRequest(String name,
                       BigDecimal price,
                       Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuRequest(Long id,
                       String name,
                       BigDecimal price,
                       Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toDomain() {

        return new Menu(name, price, null, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Long> productIds() {
        return menuProducts.stream()
                .map(it -> it.getProductId())
                .collect(Collectors.toList());
    }

    public static class Builder {

        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductRequest> menuProducts;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder intPrice(int price) {
            this.price = BigDecimal.valueOf(price);
            return this;
        }

        public Builder menuGroup(MenuGroup menuGroup) {
            this.menuGroupId = menuGroup.getId();
            return this;
        }

        public Builder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }


        public Builder menuProductRequests(List<MenuProductRequest> menuProductRequests) {
            this.menuProducts = menuProductRequests;
            return this;
        }

        public Builder menuProductRequests(MenuProductRequest... menuProductRequests) {
            this.menuProducts = List.of(menuProductRequests);
            return this;
        }

        public MenuRequest build() {

            return new MenuRequest(id, name, price, menuGroupId, menuProducts);
        }
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
