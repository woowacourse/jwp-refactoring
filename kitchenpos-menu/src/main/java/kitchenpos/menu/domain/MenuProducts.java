package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> value = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> value) {
        this.value = value;
    }

    public void addProduct(final MenuProduct menuProduct) {
        value.add(menuProduct);
    }

    public List<MenuProduct> getValue() {
        return value;
    }
}
