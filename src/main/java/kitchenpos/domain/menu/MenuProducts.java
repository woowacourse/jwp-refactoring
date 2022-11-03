package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> value = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts, final Menu menu) {
        this.value = menuProducts;
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(menu);
        }
    }

    public List<MenuProduct> getValue() {
        return value;
    }
}
