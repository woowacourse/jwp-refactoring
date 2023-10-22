package kitchenpos.domain;

import kitchenpos.domain.vo.Money;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Money price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this(id, name, price, menuGroup, new ArrayList<>());
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(name, price, menuGroup, new ArrayList<>());
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Money(price);
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public void changeMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new MenuProducts(menuProducts);
        if (price.getValue().compareTo(this.menuProducts.menuProductsPrice()) > 0) {
            throw new IllegalArgumentException("가격의 합이 맞지 않습니다");
        }
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Menu menu = (Menu) object;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
