package kitchenpos.menu.domain;

import static kitchenpos.Price.ZERO_PRICE;

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
import kitchenpos.Price;
import kitchenpos.menugroup.domain.MenuGroup;

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

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "MENU_ID", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(final String name, final Price price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        validateMenuProducts(menuProducts);
        this.menuProducts.addAll(menuProducts);
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts) {
        final Price sum = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(ZERO_PRICE, Price::add);
        if (price.isBiggerThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 상품가격 * 수량의 합보다 작거나 같아야한다");
        }
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
