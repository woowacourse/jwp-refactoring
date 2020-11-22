package kitchenpos.domain.menu;

import kitchenpos.domain.MenuProduct;
import kitchenpos.util.ValidateUtil;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private MenuPrice menuPrice;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name, MenuPrice menuPrice, MenuGroup menuGroup) {
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroup = menuGroup;
    }

    public static Menu of(String name, MenuPrice menuPrice, MenuGroup menuGroup) {
        ValidateUtil.validateNonNullAndNotEmpty(name);
        ValidateUtil.validateNonNull(menuPrice, menuGroup);

        return new Menu(name, menuPrice, menuGroup);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getMenuPrice() {
        return menuPrice;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
