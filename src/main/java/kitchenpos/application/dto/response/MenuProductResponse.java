package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long id;
    private long quantity;

    public MenuProductResponse() {
    }

    private MenuProductResponse(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static MenuProductResponse create(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
