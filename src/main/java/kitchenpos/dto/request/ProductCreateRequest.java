package kitchenpos.dto.request;

public class ProductCreateRequest {

    private final String name;
    private final Long price;

    public ProductCreateRequest(final String name, final long price) {
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
