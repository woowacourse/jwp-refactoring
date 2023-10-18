package kitchenpos.domain.menu;

import kitchenpos.exception.MenuException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

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
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        if (isNull(menuProducts) ||
                isMenuPriceGreaterThanTotalMenuProductsPrice(menuProducts)) {
            throw new MenuException("메뉴 가격이 전체 상품 * 수량 가격의 합 보다 큽니다");
        }
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(this);
        }
        this.menuProducts = menuProducts;
    }

    private boolean isNull(List<MenuProduct> menuProducts) {
        return Objects.isNull(menuProducts);
    }

    private boolean isMenuPriceGreaterThanTotalMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalMenuProductsPrice = calculateTotalMenuProductsPrice(menuProducts);
        return price.isGreaterThan(totalMenuProductsPrice);
    }

    private BigDecimal calculateTotalMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalMenuProductsPrice = BigDecimal.valueOf(0L);
        for (MenuProduct menuProduct : menuProducts) {
            totalMenuProductsPrice = totalMenuProductsPrice.add(menuProduct.calculateTotalPrice());
        }
        return totalMenuProductsPrice;
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
        return menuProducts;
    }
}
