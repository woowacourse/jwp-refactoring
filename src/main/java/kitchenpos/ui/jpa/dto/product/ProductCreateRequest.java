package kitchenpos.ui.jpa.dto.product;

public class ProductCreateRequest {

    private String name;
    private long price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, long price) {
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
