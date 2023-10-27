package kitchenpos.menu.domain;

import kitchenpos.common.vo.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
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

    public Price calculateTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPriceValue().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return new Price(sum);
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
