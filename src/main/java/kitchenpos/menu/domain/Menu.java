package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.menu.exception.MenuPriceTooExpensiveException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    private Menu(String name, Price price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    protected Menu() {
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
        return new Menu(name, Price.from(price), menuGroupId, menuProducts);
    }

    public void changeMenuProducts(List<MenuProduct> menuProducts) {
        validateSumBiggerThanSinglePrice(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validateSumBiggerThanSinglePrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(menuProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow(RuntimeException::new);

        if (price.isLessThan(sum)) {
            throw new MenuPriceTooExpensiveException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
