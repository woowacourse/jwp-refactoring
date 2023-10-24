package kitchenpos.menu.dto;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), menu.getMenuProducts().stream()
                .map(MenuProductDto::from)
                .collect(toList()));
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }

    private static class MenuProductDto {

        private final Long seq;
        private final Long menuId;
        private final Long productId;
        private final Long quantity;

        private MenuProductDto(Long seq, Long menuId, Long productId, Long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductDto from(MenuProduct menuProduct) {
            return new MenuProductDto(menuProduct.getSeq(), menuProduct.getMenu().getId(),
                    menuProduct.getProduct().getId(), menuProduct.getQuantity());
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
}
