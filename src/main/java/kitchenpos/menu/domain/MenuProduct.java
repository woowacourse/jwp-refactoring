package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Hibernate;

import kitchenpos.domain.Menu;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Long productId, long quantity) {
        validate(quantity);
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    private void validate(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("메뉴 수량은 1이상이어야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        MenuProduct menuProduct = (MenuProduct)o;
        return seq != null && Objects.equals(seq, menuProduct.getSeq());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
