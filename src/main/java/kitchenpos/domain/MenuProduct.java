package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @GeneratedValue
    @Id
    private Long id;
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long seq, Product product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Product getProduct() {
        return product;
    }
    
    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
