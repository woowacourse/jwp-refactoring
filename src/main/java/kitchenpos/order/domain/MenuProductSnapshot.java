package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProductSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Price productPrice;

    private Long quantity;

    public MenuProductSnapshot() {
    }

    public MenuProductSnapshot(final String name, final Price productPrice, final Long quantity) {
        this.name = name;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getProductPrice() {
        return productPrice;
    }

    public Long getQuantity() {
        return quantity;
    }
}
