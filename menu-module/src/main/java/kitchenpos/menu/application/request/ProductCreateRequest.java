package kitchenpos.menu.application.request;

public class ProductCreateRequest {

    private String name;
    private String price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
