package kitchenpos.domain;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {

    }

    public Menu(Long id,
                String name,
                BigDecimal price,
                Long menuGroupId,
                MenuProducts menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void comparePrice(Products products) {
        BigDecimal totalProductPrice = menuProducts.calculateTotalPrice(products);
        validatePriceRange(totalProductPrice);
    }

    private void validatePriceRange(BigDecimal upperInclusive) {
        validateMenuPriceGreaterThanOrEqualToZero();
        validateMenuPriceLessThanOrEqualTo(upperInclusive);
    }

    private void validateMenuPriceGreaterThanOrEqualToZero() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuPriceLessThanOrEqualTo(BigDecimal upperLimit) {
        if (price.compareTo(upperLimit) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getProductIds() {
        return menuProducts.getProductIds();
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
