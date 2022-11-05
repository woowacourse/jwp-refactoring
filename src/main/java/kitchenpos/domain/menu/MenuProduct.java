package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "product_price", nullable = false))
    })
    private Price productPrice;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu,
                       final String productName,
                       final BigDecimal productPrice,
                       final Long productId,
                       final long quantity) {
        this.menu = menu;
        this.productName = productName;
        this.productPrice = Price.valueOf(productPrice);
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final String productName, final BigDecimal productPrice, final Long productId,
                       final long quantity) {
        this.productName = productName;
        this.productPrice = Price.valueOf(productPrice);
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public Price getProductPrice() {
        return productPrice;
    }

    public BigDecimal getProductPriceValue() {
        if (productPrice == null) {
            return null;
        }
        return productPrice.getValue();
    }
}
