package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> values) {
        this.values = values;
    }

    public List<MenuProduct> getValues() {
        return values;
    }

    public void addAll(final List<MenuProduct> menuProducts, final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.changeMenu(menu));
        values.addAll(menuProducts);
    }
}
