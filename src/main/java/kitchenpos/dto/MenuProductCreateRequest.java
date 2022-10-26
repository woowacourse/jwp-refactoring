package kitchenpos.dto;

public class MenuProductCreateRequest {

    private Long id;
    private long quantity;

    public MenuProductCreateRequest(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public long getQuantity() {
        return quantity;
    }
}
