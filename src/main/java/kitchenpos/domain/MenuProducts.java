package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void registerMenu(Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.registerMenu(menu);
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
