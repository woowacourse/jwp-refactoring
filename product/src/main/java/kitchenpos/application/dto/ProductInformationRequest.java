package kitchenpos.application.dto;

public class ProductInformationRequest {
    private String name;
    private Long price;

    public ProductInformationRequest() {
    }

    public ProductInformationRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
