package kitchenpos.domain.menu;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void addAll(final List<MenuProduct> toAddMenuProducts) {
        this.menuProducts.addAll(toAddMenuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
