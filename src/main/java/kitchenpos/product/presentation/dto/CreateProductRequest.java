package kitchenpos.product.presentation.dto;

public class CreateProductRequest {

    private String name;

    private long price;

    private CreateProductRequest() {
    }

    public CreateProductRequest(final String name,
                                final long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
