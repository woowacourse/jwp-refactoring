package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.MenuPriceExpensiveThanProductsPriceException;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MENU")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final Long id,
                final String name,
                final Long price,
                final MenuGroup menuGroup) {

        this.id = id;
        this.name = name;
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = new ArrayList<>();
    }

    public void initMenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuPriceMoreExpensiveThanProductsPriceSum();
        this.menuProducts = menuProducts;
    }

    private void validateMenuPriceMoreExpensiveThanProductsPriceSum() {
        long menuProductsTotalPrice = menuProducts.stream()
                .mapToLong(MenuProduct::getTotalPrice)
                .sum();

        if (price.getPrice() > menuProductsTotalPrice) {
            throw new MenuPriceExpensiveThanProductsPriceException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroup, menu.menuGroup) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}
