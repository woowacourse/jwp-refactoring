package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> values, final Menu menu) {
        addAll(values, menu);
    }

    private void addAll(final List<MenuProduct> menuProducts, final Menu menu) {
        final List<MenuProduct> menuInserted = menuProducts.stream()
                .map(menuProduct ->
                        new MenuProduct(
                                menu,
                                menuProduct.getProductName(),
                                menuProduct.getProductPriceValue(),
                                menuProduct.getProductId(),
                                menuProduct.getQuantity()
                        )
                )
                .collect(Collectors.toList());
        values.addAll(menuInserted);
    }

    public List<MenuProduct> getValues() {
        return values;
    }
}
