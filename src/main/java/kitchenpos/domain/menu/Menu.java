package kitchenpos.domain.menu;

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
        validateMenuPrice(price, productQuantities);
        return new Menu(null, name, new Price(price), menuGroupId);
    }

    private static void validateMenuPrice(BigDecimal price, ProductQuantities productQuantities) {
        BigDecimal individualPriceSum = productQuantities.calculateTotalPrice();
        if (price == null || price.compareTo(individualPriceSum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 구성품을 개별적으로 구매했을 때에 비해 비싸면 안됩니다.");
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
}
