package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.product.domain.Product;

@Entity
public class Menu {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId,
                List<MenuProduct> menuProducts) {
        validate(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validate(BigDecimal price, List<MenuProduct> menuProducts) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (price.compareTo(sum(menuProducts)) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal sum(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
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
