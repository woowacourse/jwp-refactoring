package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class CreateMenuRequest {

    private CreateMenuRequest() {
    }

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public static CreateMenuRequestBuilder builder() {
        return new CreateMenuRequestBuilder();
    }

    public static final class CreateMenuRequestBuilder {
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductRequest> menuProducts;

        private CreateMenuRequestBuilder() {
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
            return createMenuRequest;
        }
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
