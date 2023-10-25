package kitchenpos.domain;

import static kitchenpos.domain.Price.ZERO_PRICE;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_GROUP_ID")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(final String name, final Price price, final MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuProducts(menuProducts);
        menuProducts.forEach(this::addMenuProduct);
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts) {
        final Price sum = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(ZERO_PRICE, Price::add);
        if (price.isBiggerThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    private void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.setMenu(this);
        menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
