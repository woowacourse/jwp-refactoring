package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public int size() {
        return menuProducts.size();
    }

}
