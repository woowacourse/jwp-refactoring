package kitchenpos.menu.domain;

import kitchenpos.product.exception.InvalidProductPriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    
    private BigDecimal price;
    
    public MenuPrice() {
    }
    
    public MenuPrice(final BigDecimal price) {
        validate(price);
        this.price = price;
    }
    
    private void validate(final BigDecimal menuPrice) {
        if (Objects.isNull(menuPrice) || menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("메뉴 가격은 0원 이상이어야 합니다");
        }
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuPrice menuPrice1 = (MenuPrice) o;
        return price.equals(menuPrice1.price);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
    
    @Override
    public String toString() {
        return "Price{" +
                "price=" + price +
                '}';
    }
}
