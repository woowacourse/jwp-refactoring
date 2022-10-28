package kitchenpos.ui.jpa.dto.product;

public class ProductListResponse {

    private Long id;
    private String name;
    private long price;

    public ProductListResponse(Long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
