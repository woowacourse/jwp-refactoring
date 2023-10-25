package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuException;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(MenuName name, MenuPrice price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(Long id, MenuName name, MenuPrice price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts();
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.add(menuProducts);
        BigDecimal menuProductsPrice = this.menuProducts.calculateTotalMenuProductsPrice();

        if (price.isGreaterThan(menuProductsPrice)) {
            throw new MenuException("메뉴 가격이 전체 상품 * 수량 가격의 합 보다 큽니다");
        }
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(this);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
