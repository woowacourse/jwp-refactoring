package kitchenpos.domain.menu;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public void addAll(final List<MenuProduct> toAddMenuProducts) {
        this.menuProducts.addAll(toAddMenuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
