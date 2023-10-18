package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "menuId", updatable = false, nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validate(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts.addAll(menuProducts);
    }

    private void validate(BigDecimal price, List<MenuProduct> menuProducts) {
        validateSumOfMenuProductsPrice(price, menuProducts);
    }

    private void validateSumOfMenuProductsPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sumOfEachPrice = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sumOfEachPrice = sumOfEachPrice.add(menuProduct.getPrice());
        }

        if (price.compareTo(sumOfEachPrice) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 개별 상품 가격의 합보다 같거나 적어야합니다.");
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
