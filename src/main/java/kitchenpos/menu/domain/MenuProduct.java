package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;

    @Builder
    public MenuProduct(final Product product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setMenu(final Menu menu) {
        validateAccessThroughMenu(menu);
        this.menu = menu;
    }

    private void validateAccessThroughMenu(final Menu menu) {
        if (Objects.isNull(menu) || !menu.getMenuProducts().contains(this)) {
            throw new IllegalStateException();
        }
    }

    public BigDecimal calculatePrice() {
        BigDecimal price = this.product.getPrice();
        BigDecimal quantity = BigDecimal.valueOf(this.quantity);
        return price.multiply(quantity);
    }
}
