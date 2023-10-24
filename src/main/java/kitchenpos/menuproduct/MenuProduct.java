package kitchenpos.menuproduct;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;// TODO : Menu에서 List<MenuProduct>를 가지도록 하는 것이 좋지 않을까
    private Long productId;
    @Embedded
    private MenuQuantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long menuId,
                       final Long productId,
                       final MenuQuantity quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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

    public MenuQuantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
