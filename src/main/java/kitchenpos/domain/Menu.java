package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    private static final int MAX_NAME_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    private Menu(
            String name,
            BigDecimal price,
            MenuGroup menuGroup
    ) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        validateName(name);
        validatePrice(price);
        validateMenuGroup(menuGroup);

        return new Menu(name, price, menuGroup);
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }

        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException();
        }

        if (price.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateMenuGroup(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
