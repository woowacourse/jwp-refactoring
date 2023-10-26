package kitchenpos.domain.menu;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity) {
        this.seq = seq;
        this.productId = productId;
        if (quantity <= 0) {
            throw new IllegalArgumentException("메뉴에 등록된 상품의 수량은 0 이하일 수 없습니다.");
        }
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
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
