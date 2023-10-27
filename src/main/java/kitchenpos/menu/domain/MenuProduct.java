package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.domain.vo.MenuProductName;
import kitchenpos.menu.domain.vo.MenuProductPrice;
import kitchenpos.menu.domain.vo.MenuQuantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long productId;

    @Embedded
    private MenuProductName menuProductName;

    @Embedded
    private MenuProductPrice menuProductPrice;

    @Embedded
    private MenuQuantity menuQuantity;

    protected MenuProduct() {
    }

    public MenuProduct(
            Long productId,
            String productName,
            BigDecimal productPrice,
            long quantity
    ) {
        this.productId = productId;
        this.menuProductName = new MenuProductName(productName);
        this.menuProductPrice = new MenuProductPrice(productPrice);
        this.menuQuantity = new MenuQuantity(quantity);
    }

    public BigDecimal calculatePrice() {
        return menuProductPrice.multiply(menuQuantity.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return menuProductName.getName();
    }

    public BigDecimal getProductPrice() {
        return menuProductPrice.getPrice();
    }

    public long getQuantity() {
        return menuQuantity.getQuantity();
    }
}
