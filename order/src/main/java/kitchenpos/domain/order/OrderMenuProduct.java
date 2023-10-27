package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import kitchenpos.support.money.Money;

@Embeddable
public class OrderMenuProduct {

    private String name;
    private Money price;
    private long quantity;

    public OrderMenuProduct() {
    }

    public OrderMenuProduct(String name, Money price, long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
