package kitchenpos.dto;

public class ProductResponse {
    private Long id;

    private ProductResponse() {
    }

    public ProductResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
