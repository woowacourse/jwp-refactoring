package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void addMenuProducts(final Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(menu);
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
