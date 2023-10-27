package kitchenpos.menu.domain;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @Embedded
    private Quantity quantity;

    protected MenuProductHistory() {
    }

    protected MenuProductHistory(final Name name, final Price price, final Quantity quantity) {
        this(null, name, price, quantity);
    }

    protected MenuProductHistory(final Long seq, final Name name, final Price price, final Quantity quantity) {
        this.seq = seq;
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

    public Long getSeq() {
        return seq;
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
