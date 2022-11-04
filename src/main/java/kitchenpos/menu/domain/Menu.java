package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private MenuPrice menuPrice;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(Long id, String name, MenuPrice menuPrice, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(this);
        }
    }

    private Menu(String name, MenuPrice menuPrice, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, menuPrice, menuGroupId, menuProducts);
    }

    public static Menu create(String name,
                              MenuPrice menuPrice,
                              Long menuGroupId,
                              List<MenuProduct> menuProducts,
                              MenuValidator menuValidator) {
        menuValidator.validate(menuGroupId, menuPrice, menuProducts);
        return new Menu(name, menuPrice, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return menuPrice;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
