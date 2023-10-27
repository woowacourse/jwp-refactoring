package kitchenpos.menu.domain;

import com.sun.istack.NotNull;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private MenuQuantity quantity;

    @NotNull
    private Long productId;

    protected MenuProduct() {
    }

    private MenuProduct(Long quantity, Long productId) {
        this.quantity = MenuQuantity.from(quantity);
        this.productId = productId;
    }

    public static MenuProduct create(Long quantity, Long productId) {
        validateProduct(productId);

        return new MenuProduct(quantity, productId);
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
        return quantity.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }
}
