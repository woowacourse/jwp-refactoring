package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, Map<Product, Long> menuProducts) {
        validate(name, price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        for (Product product : menuProducts.keySet()) {
            MenuProduct menuProduct = new MenuProduct(this, product, menuProducts.get(product));
            this.menuProducts.add(menuProduct);
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

    private void validate(String name, BigDecimal price, Map<Product, Long> menuProducts) {
        validateName(name);
        validatePrice(price);
        validateProperPrice(price, menuProducts);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("메뉴의 이름은 비어있을 수 없습니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴의 가격은 비어있을 수 없습니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 0원 미만일 수 없습니다.");
        }
    }

    private void validateProperPrice(BigDecimal price, Map<Product, Long> menuProducts) {
        BigDecimal sum = menuProducts.keySet().stream()
            .map(it -> it.getPrice().multiply(BigDecimal.valueOf(menuProducts.get(it))))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("각 상품 가격의 합보다 큰 가격을 적용할 수 없습니다.");
        }
    }

}
