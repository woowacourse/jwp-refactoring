package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.domain.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> value;

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        if (menuProducts == null) {
            this.value = new ArrayList<>();
            return;
        }
        this.value = menuProducts;
    }

    public void validatePriceIsLowerThanTotalPrice(final Price price) {
        final BigDecimal total = getTotalPrice();
        if (price.isHigherThan(total)) {
            throw new IllegalArgumentException(String.format(
                "메뉴의 가격은 상품의 총 합보다 같거나 작아야 합니다. [Menu Price : %s / total : %s]", price, total
            ));
        }
    }

    private BigDecimal getTotalPrice() {
        return value.stream()
            .map(MenuProduct::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getValue() {
        return Collections.unmodifiableList(value);
    }
}
