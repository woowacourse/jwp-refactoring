package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.OrderMenuProduct;

public class OrderMenuProductResponse {

    private Long seq;
    private String productName;
    private BigDecimal productPrice;
    private long quantity;

    public OrderMenuProductResponse(final Long seq, final String productName, final BigDecimal productPrice,
                                    final long quantity) {
        this.seq = seq;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public static OrderMenuProductResponse from(final OrderMenuProduct orderMenuProduct) {
        return new OrderMenuProductResponse(orderMenuProduct.getSeq(), orderMenuProduct.getProductName(),
                orderMenuProduct.getProductPrice(),
                orderMenuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
