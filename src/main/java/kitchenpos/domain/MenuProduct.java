package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn
    private Menu menu;
    private Long productId;
    private long quantity;

    // TODO: 20. 11. 17. protected 바꾸기
    public MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
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
}
