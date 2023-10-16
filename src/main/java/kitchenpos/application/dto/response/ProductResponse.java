package kitchenpos.application.dto.response;

import kitchenpos.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final String price;

    private ProductResponse(Long id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().toString())
                .build();
    }

    public static ProductResponseBuilder builder() {
        return new ProductResponseBuilder();
    }

    public static final class ProductResponseBuilder {
        private Long id;
        private String name;
        private String price;

        private ProductResponseBuilder() {
        }

        public ProductResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductResponseBuilder price(String price) {
            this.price = price;
            return this;
        }

        public ProductResponse build() {
            return new ProductResponse(id, name, price);
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
