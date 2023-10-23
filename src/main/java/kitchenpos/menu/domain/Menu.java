package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.exception.MenuPriceTooExpensiveException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    private Menu(String name, Price price, MenuGroup menuGroup,
            List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    protected Menu() {
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup,
            List<MenuProduct> menuProducts) {
        return new Menu(name, Price.from(price), menuGroup, menuProducts);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
