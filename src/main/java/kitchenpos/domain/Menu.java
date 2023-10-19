package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(
            String name,
            Price price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(
            Long id,
            String name,
            Price price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(
            String name,
            BigDecimal price,
            MenuGroup menuGroup
    ) {
        return new Menu(
                name,
                Price.from(price),
                menuGroup,
                new ArrayList<>()
        );
    }

    public void addMenuProduct(MenuProduct menuProduct) {

//        if (a.getMenu().equals(find)) {
//            return;
//        }

//        menuProducts.add(menuProduct);
        menuProduct.registerMenu(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
