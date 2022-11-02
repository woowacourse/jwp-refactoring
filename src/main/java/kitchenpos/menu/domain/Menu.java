package kitchenpos.menu.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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

    @JoinColumn(name = "menu_group_id")
    @ManyToOne(fetch = LAZY)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = PERSIST)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name,
                BigDecimal price,
                MenuGroup menuGroup,
                List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void mapMenuProducts(List<MenuProduct> menuProducts) {

        this.menuProducts = menuProducts;

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(this);
        }
    }

    public void mapMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public void validate() {
        validatePrice();
        validatePriceBetweenSelfAndProducts();
    }

    private void validatePrice() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 양의 정수이어야 합니다.");
        }
    }

    public void validatePriceBetweenSelfAndProducts() {

        if (price.compareTo(totalProductsPrice()) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 상품들의 총 가격보다 클 수 없습니다.");
        }
    }

    public List<Product> products() {

        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct())
                .collect(Collectors.toList());
    }

    protected BigDecimal totalProductsPrice() {

        return menuProducts.stream()
                .map(MenuProduct::priceSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
