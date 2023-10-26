package kitchenpos.dto.product;

public class CreateProductRequest {
    private final String name;
    private final long price;

    private CreateProductRequest(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public static CreateProductRequest of(final String name, final long price) {
        return new CreateProductRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
