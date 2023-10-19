package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_Id")
    @Column(updatable = false, nullable = false)
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {
    }

    protected MenuProducts(List<MenuProduct> values) {
        this.values = values;
    }

    public static MenuProducts create(List<MenuProduct> menuProducts, BigDecimal price) {
        validate(menuProducts, price);
        return new MenuProducts(menuProducts);
    }

    private static void validate(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sumOfEachPrice = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sumOfEachPrice = sumOfEachPrice.add(menuProduct.getPrice());
        }

        if (price.compareTo(sumOfEachPrice) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 개별 상품 가격의 합보다 같거나 적어야합니다.");
        }
    }

    public List<MenuProduct> getValues() {
        return values;
    }
}
