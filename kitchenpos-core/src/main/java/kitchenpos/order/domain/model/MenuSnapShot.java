package kitchenpos.order.domain.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.global.Price;

@Embeddable
public class MenuSnapShot {

    private String name;

    @Embedded
    private Price price;

    protected MenuSnapShot() {
    }

    public MenuSnapShot(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
