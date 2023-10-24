package kitchenpos.domain.menu;

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
import kitchenpos.domain.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(final String name, final Integer price, final Long menuGroupId) {
        this(null, name, Price.from(price), menuGroupId);
    }

    public void validateOverPrice(final BigDecimal productSumPrice) {
        if (price.isBigger(productSumPrice)) {
            throw new IllegalArgumentException("메뉴 금액은 상품의 합보다 클 수 없습니다.");
        }
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.setMenu(this);
        menuProducts.add(menuProduct);
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
