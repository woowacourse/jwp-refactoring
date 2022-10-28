package kitchenpos.ui.jpa.dto.product;

public class ProductCreateResponse {

    private Long id;
    private String name;
    private long price;

    public ProductCreateResponse(Long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
