package kitchenpos.domain.menu;

import kitchenpos.domain.menu_product.MenuProduct;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuProduct> values;

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.values = menuProducts;
    }

    public MenuProducts() {
        this(Collections.emptyList());
    }

    public List<MenuProduct> getValues() {
        return Collections.unmodifiableList(values);
    }
}
