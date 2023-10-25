package kitchenpos.menu.domain.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.menu.domain.MenuProduct;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public boolean isPriceLessThan(BigDecimal price) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.compareTo(price) < 0;
    }
}
