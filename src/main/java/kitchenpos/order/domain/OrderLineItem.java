package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

@Entity
public class OrderLineItem {

    private static final int MIN_QUANTITY = 1;
    private static final int MIN_PRICE = 0;
    private static final int MAX_NAME_LENGTH = 255;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    private String name;

    @Column(precision = 19, scale = 2)
    @NotNull
    private BigDecimal price;

    @NotNull
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(String name, BigDecimal price, Long quantity) {
        validate(name, price, quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    private void validate(String name, BigDecimal price, Long quantity) {
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new NullPointerException("주문 메뉴 금액은 null일 수 없습니다.");
        }
        if (price.doubleValue() < MIN_PRICE) {
            throw new IllegalArgumentException("주문 메뉴 금액은 0원 이상이어야 합니다.");
        }
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name) || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("주문 메뉴 이름은 1글자 이상, 255자 이하여야 합니다.");
        }
    }

    private void validateQuantity(Long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("주문 메뉴의 수량은 1개 이상어이야 합니다.");
        }
    }

    public static OrderLineItem create(String name, BigDecimal price, Long quantity) {
        return new OrderLineItem(name, price, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }
}
