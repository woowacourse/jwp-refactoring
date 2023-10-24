package kitchenpos.application.dto.response;

import kitchenpos.domain.menu.Menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .menuGroupId(menu.getMenuGroupId())
                .menuProducts(MenuProductResponse.from(menu))
                .build();
    }

    public static MenuResponseBuilder builder() {
        return new MenuResponseBuilder();
    }

    public static final class MenuResponseBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductResponse> menuProducts;

        private MenuResponseBuilder() {
        }

        public MenuResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuResponseBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuResponseBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuResponseBuilder menuProducts(List<MenuProductResponse> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public MenuResponse build() {
            return new MenuResponse(id, name, price, menuGroupId, menuProducts);
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
