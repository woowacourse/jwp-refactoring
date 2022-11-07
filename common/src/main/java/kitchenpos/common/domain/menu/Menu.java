package kitchenpos.common.domain.menu;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Price price;

    @Column
    private Long menuGroupId;

    protected Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, ProductQuantities productQuantities) {
        Price menuPrice = new Price(price);
        validateMenuPrice(menuPrice, productQuantities);
        return new Menu(null, name, menuPrice, menuGroupId);
    }

    private static void validateMenuPrice(Price menuPrice, ProductQuantities productQuantities) {
        Price totalPrice = productQuantities.calculateTotalPrice();
        if (menuPrice == null || menuPrice.isBiggerThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격은 구성품을 개별적으로 구매했을 때에 비해 비싸면 안됩니다.");
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

    public BigDecimal getPriceValue() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
