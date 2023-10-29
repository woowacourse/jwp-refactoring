package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.service.MenuProductDto;

public enum MenuProductFixture {

    FRIED_CHICKEN_MENU_PRODUCT(1L, 1L, ProductFixture.FRIED_CHICKEN.id, 2L),
    SPICY_CHICKEN_MENU_PRODUCT(2L, 2L, ProductFixture.SPICY_CHICKEN.id, 1L);

    public final Long seq;
    public final Long menuId;
    public final Long productId;
    public final long quantity;

    MenuProductFixture(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductDto toDto() {
        MenuProductDto menuProductDto = new MenuProductDto();
        menuProductDto.setSeq(seq);
        menuProductDto.setMenuId(menuId);
        menuProductDto.setProductId(productId);
        menuProductDto.setQuantity(quantity);
        return menuProductDto;
    }

    public MenuProduct toEntity(Product product) {
        return new MenuProduct(seq, product.getName(), product.getPrice(), quantity);
    }
}
