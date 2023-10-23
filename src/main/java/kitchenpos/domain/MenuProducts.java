package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        validate(menuProducts);

        return new MenuProducts(menuProducts);
    }

    private static void validate(List<MenuProduct> menuProducts) {
        if (menuProducts == null) {
            throw new NullPointerException("메뉴 상품은 null일 수 없습니다.");
        }
    }

    public BigDecimal getTotalPriceOfMenuProducts() {
        double totalPrice = menuProducts.stream()
                .mapToDouble(menuProduct -> menuProduct.getMenuProductPrice().doubleValue())
                .sum();

        return BigDecimal.valueOf(totalPrice);
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }


    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
