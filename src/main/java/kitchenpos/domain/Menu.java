package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class Menu {

    @GeneratedValue
    @Id
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "MENU_ID")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validate(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validate(BigDecimal price, List<MenuProduct> menuProducts) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) throw new IllegalArgumentException();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
