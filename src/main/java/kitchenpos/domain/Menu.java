package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Menu {
    private static final int COMPARE_RESULT_EQUAL = 0;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < COMPARE_RESULT_EQUAL) {
            throw new IllegalArgumentException();
        }
    }

    public void validatePrice(BigDecimal productTotalPrice) {
        if (isPriceLessThan(productTotalPrice)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isPriceLessThan(BigDecimal productTotalPrice) {
        return this.price.compareTo(productTotalPrice) > COMPARE_RESULT_EQUAL;
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

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }
}
