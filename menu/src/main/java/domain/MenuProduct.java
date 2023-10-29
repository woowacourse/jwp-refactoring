package domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
    @JoinColumn(nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Price price;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, String name, Price price, long quantity) {
        this(null, menu, name, price, quantity);
    }

    public MenuProduct(Long seq, Menu menu, String name, Price price, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
