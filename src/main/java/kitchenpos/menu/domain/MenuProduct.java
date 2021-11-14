package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @GeneratedValue
    @Id
    private Long id;
    private Long seq;

    @ManyToOne
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

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }
    
    public long getQuantity() {
        return quantity;
    }
}
