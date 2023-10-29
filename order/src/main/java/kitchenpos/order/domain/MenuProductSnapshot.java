package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.vo.Price;

@Entity
public class MenuProductSnapshot {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    private Long quantity;

    protected MenuProductSnapshot() {
    }

    public MenuProductSnapshot(final String name, final Price price, final Long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
