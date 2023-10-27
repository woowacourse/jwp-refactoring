package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Embedded
    private MenuProductQuantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, MenuProductQuantity quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(Long seq, Product product, MenuProductQuantity quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal calculateTotalPrice() {
        return product.getPrice().multiply(
                BigDecimal.valueOf(quantity.getQuantity()));
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(getSeq(), that.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq());
    }
}
