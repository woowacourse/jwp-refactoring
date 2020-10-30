package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        validate(name);
        validate(price);
        validate(menuGroup);
        validate(menuProducts);

        setMenu(menuProducts);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public static Menu entityOf(String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    private void validate(String name) {
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("Price는 Null일 수 없습니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price는 0보다 작을 수 없습니다.");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", menuProducts=" + menuProducts +
            '}';
    }
}
