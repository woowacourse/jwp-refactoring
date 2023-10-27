package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Price;
import suppoert.domain.BaseEntity;

@Entity
public class MenuProduct extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
    private Menu menu;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;
    private long quantity;


    public MenuProduct(final Menu menu, final String name, final Price price, final int quantity) {
        this.menu = menu;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        menu.addMenuProduct(this);
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

    public long calculatePrice() {
        return price.getPrice().longValue() * quantity;
    }

    protected MenuProduct() {
    }
}
