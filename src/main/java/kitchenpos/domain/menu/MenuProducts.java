package kitchenpos.domain.menu;

import kitchenpos.exception.MenuProductException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public void add(List<MenuProduct> menuProducts) {
        if (isNull(menuProducts)) {
            throw new MenuProductException("메뉴 상품이 존재하지 않습니다.");
        }
        this.menuProducts.addAll(menuProducts);
    }

    private boolean isNull(List<MenuProduct> menuProducts) {
        return Objects.isNull(menuProducts);
    }

    public BigDecimal calculateTotalMenuProductsPrice() {
        BigDecimal totalMenuProductsPrice = BigDecimal.valueOf(0L);
        for (MenuProduct menuProduct : menuProducts) {
            totalMenuProductsPrice = totalMenuProductsPrice.add(menuProduct.calculateTotalPrice());
        }
        return totalMenuProductsPrice;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
