package kitchenpos.domain.menu;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.List;

@AttributeOverride(name = "id", column = @Column(name = "menu_id"))
@Entity
public class Menu extends BaseEntity {
    private String name;
    @Embedded
    private MenuPrice menuPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "FK_MENU_MENU_GROUP"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(Long id, String name, MenuPrice menuPrice, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        this.id = id;
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroup = menuGroup;
    }

    public Menu(String name, MenuPrice menuPrice, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, menuPrice, menuGroup, menuProducts);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return menuPrice;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
