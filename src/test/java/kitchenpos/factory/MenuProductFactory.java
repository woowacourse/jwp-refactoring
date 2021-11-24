package kitchenpos.factory;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.dto.MenuProductRequest;

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

    public static MenuProductRequest dto(MenuProduct menuProduct) {
        return new MenuProductRequest(
            menuProduct.getSeq(),
            menuProduct.getMenuId(),
            menuProduct.getProductId(),
            menuProduct.getQuantity()
        );
    }

    public static List<MenuProductRequest> dtoList(MenuProducts menuProducts) {
        return menuProducts.toList().stream()
            .map(MenuProductFactory::dto)
            .collect(Collectors.toList());
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
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}
