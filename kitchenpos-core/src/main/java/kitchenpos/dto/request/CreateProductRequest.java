package kitchenpos.dto.request;

public class CreateProductRequest {
    private CreateProductRequest() {
    }

    private String name;
    private String price;

    public static CreateProductRequestBuilder builder() {
        return new CreateProductRequestBuilder();
    }

    public static final class CreateProductRequestBuilder {
        private String name;
        private String price;

        private CreateProductRequestBuilder() {
        }

        public CreateProductRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateProductRequestBuilder price(String price) {
            this.price = price;
            return this;
        }

        public CreateProductRequest build() {
            CreateProductRequest createProductRequest = new CreateProductRequest();
            createProductRequest.name = this.name;
            createProductRequest.price = this.price;
            return createProductRequest;
        }
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
