package kitchenpos.menu;

import kitchenpos.common.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "price", precision = 19, scale = 2, nullable = false)
    )
    private Price price;
    private Long menuGroupId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "menu_id",
            nullable = false
    )
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        validateNotExceeded(price, menuProducts);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        validateNotExceeded(price, menuProducts);
    }

    private void validateNotExceeded(Price menuPrice, List<MenuProduct> menuProducts) {
        Price menuProductsTotal = menuProducts.stream()
                .map(MenuProduct::evaluate)
                .reduce(Price.ZERO, Price::add);

        if (menuPrice.isGreaterThan(menuProductsTotal)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 합계를 넘을 수 없습니다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
