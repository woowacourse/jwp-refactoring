package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.menu.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductDtos;

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProductDtos) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
    }

    public static MenuResponse of(Menu menu, List<MenuProduct> menuProducts) {
        Long id = menu.getId();
        String name = menu.getName();
        BigDecimal price = menu.getMenuPrice().getValue();
        Long menuGroupId = menu.getMenuGroup().getId();
        List<MenuProductDto> menuProductResponses = menuProducts.stream()
                .map(MenuProductDto::from)
                .collect(Collectors.toList());

        return new MenuResponse(id, name, price, menuGroupId, menuProductResponses);
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

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }

    public static class MenuProductDto {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        private MenuProductDto(Long seq, Long menuId, Long productId, long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductDto from(MenuProduct menuProduct) {
            Long seq = menuProduct.getSeq();
            Long menuId = menuProduct.getMenu().getId();
            Long productId = menuProduct.getProduct().getId();
            long quantity = menuProduct.getQuantity();

            return new MenuProductDto(seq, menuId, productId, quantity);
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

        public long getQuantity() {
            return quantity;
        }
    }
}
