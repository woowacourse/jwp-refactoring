package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductAssembler {

    private MenuProductAssembler() {
    }

    public static MenuProduct assemble(MenuProductRequest menuProductRequest, Product product) {
        return MenuProduct.entityOf(product, menuProductRequest.getQuantity());
    }
}
