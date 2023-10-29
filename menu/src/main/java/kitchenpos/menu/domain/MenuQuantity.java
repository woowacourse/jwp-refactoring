package kitchenpos.menu.domain;

import com.sun.istack.NotNull;
import javax.persistence.Embeddable;

@Embeddable
public class MenuQuantity {

    private static final int MIN_QUANTITY = 1;

    @NotNull
    private Long quantity;

    public MenuQuantity() {
    }

    public MenuQuantity(Long quantity) {
        validate(quantity);

        this.quantity = quantity;
    }

    private static void validate(Long quantity) {
        if (quantity == null) {
            throw new NullPointerException("상품 수량은 null일 수 없습니다.");
        }
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("상품 개수는 1개 이상이어야 합니다.");
        }
    }

    public static MenuQuantity from(Long quantity) {
        return new MenuQuantity(quantity);
    }

    public Long getQuantity() {
        return quantity;
    }
}
