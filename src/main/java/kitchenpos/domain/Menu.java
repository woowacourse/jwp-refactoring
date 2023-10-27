package kitchenpos.domain;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private MenuPrice price;
    @Column(name = "menu_group_id")
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", orphanRemoval = true, cascade = {PERSIST, MERGE, REMOVE})
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(
        Long id,
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProduct> menuProducts
    ) {
        validatePrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts == null ? new ArrayList<>() : menuProducts;
        for (MenuProduct menuProduct : this.menuProducts) {
            menuProduct.setMenu(this);
        }
    }

    private void validatePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = calculateProductPriceSum(menuProducts);

        if (price.compareTo(sum) > 0) {
            throw new CustomException(ExceptionType.MENU_PRICE_OVER_SUM);
        }
    }

    private BigDecimal calculateProductPriceSum(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculatePriceSum());
        }

        return sum;
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public static class Builder {

        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public Builder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(id, name, price, menuGroupId, menuProducts);
        }
    }
}
