package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    
    @ManyToOne
    private Menu menu;
    
    @ManyToOne
    private Product product;
    
    @Embedded
    private Quantity quantity;
    
    public MenuProduct(final Menu menu,
                       final Product product,
                       final Quantity quantity) {
        this(null, menu, product, quantity);
    }
    
    public MenuProduct(final Long seq,
                       final Menu menu,
                       final Product product,
                       final Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }
    
    public Long getSeq() {
        return seq;
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public Quantity getQuantity() {
        return quantity;
    }
}
