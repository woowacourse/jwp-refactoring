package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import kitchenpos.product.domain.Product;

@Access(AccessType.FIELD)
@Embeddable
public class ProductInfo {

    private String productName;

    private BigDecimal productPrice;

    protected ProductInfo() {
    }

    private ProductInfo(final String productName,
                        final BigDecimal productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public static ProductInfo from(final Product product) {
        return new ProductInfo(product.getName(),
                               product.getPrice().getValue());
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }
}
