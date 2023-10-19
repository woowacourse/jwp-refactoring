package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validate(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private void validate(BigDecimal menuPrice, List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuPrice) || menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal totalMenuProductsPrice = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menuPrice.compareTo(totalMenuProductsPrice) > 0) {
            throw new IllegalArgumentException();
        }
        this.menuProducts.addAll(menuProducts);
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        BigDecimal totalMenuProductsPrice = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.price.compareTo(totalMenuProductsPrice) > 0) {
            throw new IllegalArgumentException();
        }

        menuProducts.stream()
                .forEach(menuProduct -> menuProduct.changeMenu(this));
        this.menuProducts.addAll(menuProducts);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
