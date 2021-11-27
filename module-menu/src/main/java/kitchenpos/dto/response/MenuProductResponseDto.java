package kitchenpos.dto.response;

public class MenuProductResponseDto {

    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    private MenuProductResponseDto() {
    }

    public MenuProductResponseDto(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
