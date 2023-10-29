package dto.response;

import domain.OrderedMenuProduct;
import java.math.BigDecimal;

public class OrderedMenuProductResponse {

    private Long seq;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderedMenuProductResponse() {
    }

    private OrderedMenuProductResponse(Long seq, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderedMenuProductResponse from(OrderedMenuProduct orderedMenuProduct) {
        Long seq = orderedMenuProduct.getSeq();
        String name = orderedMenuProduct.getName();
        BigDecimal price = orderedMenuProduct.getPrice().getValue();
        long quantity = orderedMenuProduct.getQuantity();
        return new OrderedMenuProductResponse(seq, name, price, quantity);
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

    public long getQuantity() {
        return quantity;
    }
}
