package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateName(name);
        validatePrice(price, menuProducts);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.assignMenu(this);
        }
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() > 255) {
            throw new IllegalArgumentException("메뉴의 이름이 유효하지 않습니다.");
        }
    }

    private void validatePrice(BigDecimal menuPrice, List<MenuProduct> menuProducts) {
        BigDecimal sumOfProductPrices = calculateSumOf(menuProducts);

        if (Objects.isNull(menuPrice)
                || menuPrice.compareTo(BigDecimal.ZERO) < 0
                || menuPrice.compareTo(BigDecimal.valueOf(Math.pow(10, 20))) >= 0
                || menuPrice.compareTo(sumOfProductPrices) > 0
        ) {
            throw new IllegalArgumentException("메뉴의 가격이 유효하지 않습니다.");
        }
    }

    private BigDecimal calculateSumOf(List<MenuProduct> menuProducts) {
        BigDecimal sumOfProductPrices = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            BigDecimal productPrice = menuProduct.getProduct().getPrice();
            long productQuantity = menuProduct.getQuantity();

            sumOfProductPrices = sumOfProductPrices.add(productPrice.multiply(BigDecimal.valueOf(productQuantity)));
        }
        return sumOfProductPrices;
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
}
