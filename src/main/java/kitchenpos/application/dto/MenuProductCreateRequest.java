package kitchenpos.application.dto;

public class MenuProductCreateRequest {
    
    private final Long productId;
    
    private final long quantity;
    
    public MenuProductCreateRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public long getQuantity() {
        return quantity;
    }
}
