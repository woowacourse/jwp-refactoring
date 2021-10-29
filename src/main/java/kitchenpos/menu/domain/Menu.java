package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        insertMenuProducts(menuProducts);
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, new MenuProducts(menuProducts));
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, price, menuGroupId, new MenuProducts());
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
        return menuProducts.toList();
    }

    public void insertMenuProducts(MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
        for (MenuProduct menuProduct : menuProducts.toList()) {
            menuProduct.defineMenu(this);
        }
    }

    public void validateMenuPrice() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격을 입력하지 않으셨거나, 적절하지 않은 가격을 입력하셨습니다.");
        }
    }

    public void validateTotalMenuProductsPrice(BigDecimal totalMenuProductsPrice) {
        if (price.compareTo(totalMenuProductsPrice) > 0) {
            throw new IllegalArgumentException("메뉴 가격이, 해당 매뉴의 전체 구성 상품을 합친 가격이보다 낮아야 합니다.");
        }
    }
}
