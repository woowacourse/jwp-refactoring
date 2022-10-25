package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id", length = 20, nullable = false)
    private Long menuId;

    @Column(name = "product_id", length = 20, nullable = false)
    private Long productId;

    @Column(length = 20, nullable = false)
    private long quantity;

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this(null, menuId, productId, quantity);
    }

    public void belongsTo(final Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuProduct menuProduct)) {
            return false;
        }
        return Objects.equals(seq, menuProduct.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
