package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProductGroup {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProductGroup() {}

    public static MenuProductGroup of(List<MenuProduct> menuProducts) {
        final MenuProductGroup menuProductGroup = new MenuProductGroup();
        menuProductGroup.menuProducts = menuProducts;
        return menuProductGroup;
    }

    public BigDecimal totalSum(BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(price.multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }


    public List<MenuProduct> value() {
        return menuProducts;
    }

    public void addMenu(Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.addMenu(menu);
        }
    }
}
