package kitchenpos.menu.domain.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.menu.domain.MenuProduct;
import org.hibernate.annotations.BatchSize;

@Embeddable
public class MenuProducts {
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public BigDecimal totalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addAll(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts;
    }
}
