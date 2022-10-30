package kitchenpos.application.dto;

public class ProductCreateRequest {

    private String name;
    private int price;

    protected ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
