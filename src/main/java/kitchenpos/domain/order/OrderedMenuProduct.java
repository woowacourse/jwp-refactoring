package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Price;

@Entity
public class OrderedMenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(
            name = "ordered_menu_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ordered_menu_product_ordered_menu")
    )
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderedMenu orderedMenu;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private long quantity;

    public OrderedMenuProduct() {
    }

    public OrderedMenuProduct(
            final OrderedMenu orderedMenu,
            final String name,
            final Price price,
            final long quantity
    ) {
        this.orderedMenu = orderedMenu;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long seq() {
        return seq;
    }

    public OrderedMenu orderedMenu() {
        return orderedMenu;
    }

    public String name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public long quantity() {
        return quantity;
    }
}
