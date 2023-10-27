package kitchenpos.menu;

import kitchenpos.product.Price;
import kitchenpos.product.PriceConverter;

import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "menu")
@Entity
public class Menu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @Convert(converter = PriceConverter.class)
    private Price price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = null;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public static Menu createWithValidation(Menu menu, MenuCreateValidator menuCreateValidator) {
        menuCreateValidator.validate(menu);
        return menu;
    }

    public void validateMenuProductsPrice(MenuProductPriceMultiplier multiplier) {
        Price totalPrice = this.menuProducts.calculateTotalPrice(multiplier);
        if (price.isBigger(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 항목의 가격 합보다 작아야한다.");
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
        return menuProducts.getCollection();
    }
}
