package kitchenpos.menu.domain.model;

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
import kitchenpos.global.Price;
import kitchenpos.menu.domain.service.MenuValidator;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu create(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts,
                              MenuValidator menuValidator) {
        Menu menu = new Menu(name, price, menuGroup, menuProducts);
        menuValidator.validate(menu);
        return menu;
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
