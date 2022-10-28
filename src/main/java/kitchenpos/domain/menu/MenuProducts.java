package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.Getter;

@Embeddable
@Getter
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> value = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        value.addAll(menuProducts);
    }

    public BigDecimal calculateEntirePrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : value) {
            sum = sum.add(menuProduct.calculatePrice());
        }
        return sum;
    }
}
