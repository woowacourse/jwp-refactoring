package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품은 한 개 이상입니다.");
        }
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public Price totalPrice() {
        final List<Price> prices = menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .collect(Collectors.toList());
        return Price.sum(prices);
    }

    public void add(final MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
