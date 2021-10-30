package kitchenpos.factory;

import kitchenpos.domain.MenuProduct;

public class MenuProductFactory {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductFactory() {

    }

    private MenuProductFactory(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductFactory builder() {
        return new MenuProductFactory();
    }

    public static MenuProductFactory copy(MenuProduct menuProduct) {
        return new MenuProductFactory(
                menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    public MenuProductFactory seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public MenuProductFactory menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public MenuProductFactory productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public MenuProductFactory quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
