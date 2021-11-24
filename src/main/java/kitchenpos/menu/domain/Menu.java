package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuGroupId;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    @Embedded
    private Price price;

    private String name;

    public Menu(Long id, String name, Price price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.belongsTo(this);
        }
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu() {
    }

    public Long getId() {
        return id;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
