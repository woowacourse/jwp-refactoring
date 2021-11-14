package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.exception.InvalidMenuProductException;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, Long quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(Long seq, Menu menu, Product product, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
        validateNull(this.menu);
        validateNull(this.product);
        validateQuantity(this.quantity);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new InvalidMenuProductException("MenuProduct 정보에 null이 포함되었습니다.");
        }
    }

    private void validateQuantity(Long quantity) {
        validateNull(quantity);
        if (quantity < 0) {
            throw new InvalidMenuProductException(String.format("음수 %s는 개수가 될 수 없습니다.", quantity));
        }
    }

    public BigDecimal productTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
