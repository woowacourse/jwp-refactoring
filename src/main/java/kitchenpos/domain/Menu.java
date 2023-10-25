package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_to_menu_group"))
    private MenuGroup menuGroup;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "menu_id",
            foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"),
            nullable = false, updatable = false
    )
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validateNotExceeded(price, menuProducts);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validateNotExceeded(price, menuProducts);
    }

    private void validateNotExceeded(Price menuPrice, List<MenuProduct> menuProducts) {
        Price menuProductsTotal = menuProducts.stream()
                .map(MenuProduct::evaluateTotal)
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
