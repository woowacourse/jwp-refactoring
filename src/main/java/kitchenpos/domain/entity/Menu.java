package kitchenpos.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @Embedded
    private Price price;

    protected Menu() {
    }

    public Menu(String name, MenuGroup menuGroup, List<MenuProduct> menuProducts, Price price) {
        validatePrice(menuProducts, price);
        this.name = name;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        this.price = price;
    }

    private void validatePrice(List<MenuProduct> menuProducts, Price menuPrice) {
        Price sumPrice = new Price(0);
        for (MenuProduct menuProduct : menuProducts) {
            Product product = menuProduct.getProduct();
            sumPrice = sumPrice.add(new Price(menuProduct.getQuantity() * product.getPrice()));
        }
        if (menuPrice.isExpensiveThan(sumPrice)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
