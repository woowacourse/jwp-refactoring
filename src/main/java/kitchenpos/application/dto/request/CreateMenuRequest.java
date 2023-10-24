package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class CreateMenuRequest {

    private CreateMenuRequest() {
    }

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public static CreateMenuRequestBuilder builder() {
        return new CreateMenuRequestBuilder();
    }

    public static final class CreateMenuRequestBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductRequest> menuProducts;

        private CreateMenuRequestBuilder() {
        }

        public CreateMenuRequestBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateMenuRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateMenuRequestBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public CreateMenuRequestBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public CreateMenuRequestBuilder menuProducts(List<MenuProductRequest> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public CreateMenuRequest build() {
            CreateMenuRequest createMenuRequest = new CreateMenuRequest();
            createMenuRequest.menuProducts = this.menuProducts;
            createMenuRequest.menuGroupId = this.menuGroupId;
            createMenuRequest.price = this.price;
            createMenuRequest.name = this.name;
            createMenuRequest.id = this.id;
            return createMenuRequest;
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
