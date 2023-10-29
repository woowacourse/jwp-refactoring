package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private Integer quantity;

    protected MenuProduct() {
    }

    private MenuProduct(final Long productId, final Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Long productId, final Integer quantity) {
        validate(productId, quantity);
        return new MenuProduct(productId, quantity);
    }

    private static void validate(final Long productId, final Integer quantity) {
        Objects.requireNonNull(productId);
        validateQuantity(quantity);
    }

    private static void validateQuantity(final Integer quantity) {
        if (Objects.isNull(quantity) || quantity < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
