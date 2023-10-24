package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.MenuExceptionType.PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.MenuException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void add(MenuProduct menuProduct) {
        validatePrice(menuProduct);
        menuProducts.add(menuProduct);
    }

    private void validatePrice(MenuProduct menuProduct) {
        List<MenuProduct> tmp = new ArrayList<>(menuProducts);
        tmp.add(menuProduct);
        Price totalMenuProductPrice = tmp.stream()
                .map(MenuProduct::amount)
                .reduce(Price::add)
                .get();
        if (this.price.isBiggerThan(totalMenuProductPrice)) {
            throw new MenuException(PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT);
        }
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public MenuGroup menuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> menuProducts() {
        return List.copyOf(menuProducts);
    }
}
