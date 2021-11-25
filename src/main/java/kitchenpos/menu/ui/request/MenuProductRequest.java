package kitchenpos.menu.ui.request;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

    @NotNull(message = "상품의 아이디가 null입니다.")
    private Long productId;

    @Min(value = 0, message = "메뉴 상품의 개수는 음수일 수 없습니다.")
    private Long quantity;

    protected MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductRequest> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductRequest::from)
            .collect(toList());
    }

    private static MenuProductRequest from(MenuProduct menuProduct) {
        return new MenuProductRequest(
            menuProduct.getProductId(),
            menuProduct.getQuantity()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
