package kitchenpos.application.dto.response;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

public class CreateMenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    private CreateMenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static CreateMenuResponse from(Menu created) {
        return CreateMenuResponse.builder()
                .id(created.getId())
                .name(created.getName())
                .price(created.getPrice())
                .menuGroupId(created.getMenuGroupId())
                .menuProducts(MenuProductResponse.from(created))
                .build();
    }

    public static CreateMenuResponseBuilder builder() {
        return new CreateMenuResponseBuilder();
    }

    public static final class CreateMenuResponseBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductResponse> menuProducts;

        private CreateMenuResponseBuilder() {
        }

        public CreateMenuResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateMenuResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateMenuResponseBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public CreateMenuResponseBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public CreateMenuResponseBuilder menuProducts(List<MenuProductResponse> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public CreateMenuResponse build() {
            return new CreateMenuResponse(id, name, price, menuGroupId, menuProducts);
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
