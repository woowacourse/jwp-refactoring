package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.InvalidMenuPriceException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Price price;

    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> products;

    protected Menu() {
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> products) {
        validatePrice(price, products);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.products = products;
        for (MenuProduct menuProduct : products) {
            menuProduct.setMenu(this);
        }
    }

    private void validatePrice(Price price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.isHigher(sum)) {
            throw new InvalidMenuPriceException();
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getProducts() {
        return products;
    }
}
