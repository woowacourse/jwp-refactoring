package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.base.BaseIdEntity;
import kitchenpos.domain.menugroup.MenuGroup;

@Entity
public class Menu extends BaseIdEntity {

    @Column(nullable = false)
    private MenuName name;

    @Embedded
    private MenuPrice price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(Long id, MenuName name, MenuPrice price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        super(id);

        validate(menuGroup, menuProducts);
        setMenu(menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        return new Menu(id, new MenuName(name), new MenuPrice(price), menuGroup, menuProducts);
    }

    public static Menu entityOf(String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        return of(null, name, price, menuGroup, menuProducts);
    }

    private void validate(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validate(menuGroup);
        validate(menuProducts);
    }

    private void validate(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException("MenuGroup은 Null일 수 없습니다.");
        }
    }

    private void validate(List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts)) {
            throw new IllegalArgumentException("MenuProducts는 Null일 수 없습니다.");
        }

        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("MenuProducts는 Empty일 수 없습니다.");
        }
    }

    private void setMenu(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(this);
        }
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", menuProducts=" + menuProducts +
            '}';
    }
}
