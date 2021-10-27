package kitchenpos.dto;

public class MenuProductDto {
    private Long seq;
    private Long productId;
    private long quantity;
    private Long menuId;

    public MenuProductDto() {
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }
}
