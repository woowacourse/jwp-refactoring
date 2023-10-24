package kitchenpos.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    @JsonProperty
    private Long seq;
    @JsonProperty
    private Long menuId;
    @JsonProperty
    private Long productId;
    @JsonProperty
    private long quantity;

    public MenuProductResponse(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> from(final List<MenuProduct> menuProducts) {
        return menuProducts
                .stream().map(menuProduct -> new MenuProductResponse(
                        menuProduct.getSeq(),
                        menuProduct.getMenuId(),
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()
                )).collect(Collectors.toList());
    }
}
