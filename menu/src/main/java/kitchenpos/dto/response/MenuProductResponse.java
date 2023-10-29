package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private String name;
    private BigDecimal price;
    private long quantity;

    public MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        Long seq = menuProduct.getSeq();
        String name = menuProduct.getName();
        BigDecimal price = menuProduct.getPrice().getValue();
        long quantity = menuProduct.getQuantity();
        return new MenuProductResponse(seq, name, price, quantity);
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
