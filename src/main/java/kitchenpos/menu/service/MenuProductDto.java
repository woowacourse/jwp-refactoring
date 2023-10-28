package kitchenpos.menu.service;

import java.util.Objects;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public static MenuProductDto from(MenuProduct entity, Long menuId) {
        MenuProductDto menuProductDto = new MenuProductDto();
        menuProductDto.setSeq(entity.getSeq());
        menuProductDto.setMenuId(menuId);
        menuProductDto.setQuantity(entity.getQuantity());
        return menuProductDto;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MenuProductDto that = (MenuProductDto) object;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
