package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.service.CalculateProductPriceService;
import kitchenpos.domain.vo.Price;

public class Menu {

    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu(final Long id,
                final String name,
                final Price price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu create(final String name,
                              final BigDecimal price,
                              final Long menuGroupId,
                              final List<MenuProduct> menuProducts) {
        return new Menu(null, name, Price.valueOf(price), menuGroupId, menuProducts);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
