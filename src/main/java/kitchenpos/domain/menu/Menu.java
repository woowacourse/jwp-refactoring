package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.service.CalculateProductPriceService;
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(final Long id,
                final String name,
                final Price price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        addMenuProducts(menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static Menu create(final String name,
                              final BigDecimal price,
                              final MenuGroup menuGroup,
                              final List<MenuProduct> menuProducts) {
        return new Menu(null, name, Price.valueOf(price), menuGroup, menuProducts);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts, this);
    }

    public void validateOverPrice(final CalculateProductPriceService calculateProductPriceService) {
        if (isOverPrice(calculateProductPriceService)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isOverPrice(final CalculateProductPriceService calculateProductPriceService) {
        final BigDecimal productPriceSum = calculateProductPriceService.calculateMenuProductPriceSum(getMenuProducts());
        return price.getValue()
                .compareTo(productPriceSum) > 0;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getValues();
    }
}
