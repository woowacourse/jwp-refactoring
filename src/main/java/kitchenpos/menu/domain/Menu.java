package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private MenuPrice price;

    @Column(name = "menuGroupId")
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = new MenuName(name);
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts();
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.add(menuProducts);
        BigDecimal menuProductsPrice = this.menuProducts.calculateTotalMenuProductsPrice();

        if (price.isGreaterThan(menuProductsPrice)) {
            throw new MenuException("메뉴 가격이 전체 상품 * 수량 가격의 합 보다 큽니다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
