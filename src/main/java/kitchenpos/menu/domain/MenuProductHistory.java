package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @Embedded
    private Quantity quantity;

    protected MenuProductHistory() {
    }

    protected MenuProductHistory(final Name name, final Price price, final Quantity quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    protected MenuProductHistory(final Long id, final Name name, final Price price, final Quantity quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static MenuProductHistory from(final MenuProduct menuProduct) {
        return new MenuProductHistory(
                menuProduct.getProduct().getName(),
                menuProduct.getProduct().getPrice(),
                menuProduct.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
