package kitchenpos.dto.response;

import kitchenpos.domain.product.Product;

public class CreateProductResponse {
    private final Long id;
    private final String name;
    private final String price;

    private CreateProductResponse(Long id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static CreateProductResponse from(final Product product) {
        return CreateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().toString())
                .build();
    }

    public static CreateProductResponseBuilder builder() {
        return new CreateProductResponseBuilder();
    }

    public static final class CreateProductResponseBuilder {
        private Long id;
        private String name;
        private String price;

        private CreateProductResponseBuilder() {
        }

        public CreateProductResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateProductResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateProductResponseBuilder price(String price) {
            this.price = price;
            return this;
        }

        public CreateProductResponse build() {
            return new CreateProductResponse(id, name, price);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
