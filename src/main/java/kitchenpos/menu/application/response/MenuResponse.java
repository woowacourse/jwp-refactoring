package kitchenpos.menu.application.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<CreateMenuProductResponse> menuProducts;

    public MenuResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                        final List<CreateMenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu, List<MenuProduct> menuProducts) {
        List<CreateMenuProductResponse> menuProductResponses = menuProducts.stream()
                .map(CreateMenuProductResponse::from)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductResponses
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<CreateMenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static class CreateMenuProductResponse {
        private final Long seq;
        private final Long menuId;
        private final Long productId;
        private final long quantity;

        public CreateMenuProductResponse(final Long seq, final Long menuId, final Long productId,
                                         final long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static CreateMenuProductResponse from(final MenuProduct menuProduct) {
            return new CreateMenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity());
        }
    }
}
