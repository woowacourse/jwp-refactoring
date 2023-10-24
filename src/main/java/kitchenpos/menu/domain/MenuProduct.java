package kitchenpos.menu.domain;

import com.sun.istack.NotNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    private static final int MIN_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    private Long quantity;

    @NotNull
    private Long productId;

    protected MenuProduct() {
    }

    private MenuProduct(Long quantity, Long productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public static MenuProduct create(Long quantity, Long productId) {
        validateQuantity(quantity);
        validateProduct(productId);

        return new MenuProduct(quantity, productId);
    }

    private static void validateQuantity(Long quantity) {
        if (quantity == null) {
            throw new NullPointerException("상품 수량은 null일 수 없습니다.");
        }
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("상품 개수는 1개 이상이어야 합니다.");
        }
    }

    private static void validateProduct(Long productId) {
        if (productId == null) {
            throw new NullPointerException("상품은 null일 수 없습니다.");
        }
    }
    
    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
