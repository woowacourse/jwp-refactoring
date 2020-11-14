package kitchenpos.domain;

import kitchenpos.domain.event.ValidateMenuPriceEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    private Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu create(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProduct> menuProducts,
            ApplicationEventPublisher publisher
    ) {
        final Menu menu = new Menu(null, name, price, menuGroupId, menuProducts);
        validate(menu, publisher);
        return menu;
    }

    public static Menu from(
            @NonNull Long id,
            String name,
            BigDecimal price,
            Long menuGroupId
    ) {
        return new Menu(id, name, price, menuGroupId, new ArrayList<>());
    }

    private static void validate(Menu menu, ApplicationEventPublisher publisher) {
        if (Objects.isNull(menu.price) || menu.price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        publisher.publishEvent(new ValidateMenuPriceEvent(menu));
    }

    public boolean isExpensiveThanSumOf(Map<Long, BigDecimal> productPrice) {
        final BigDecimal sumOfMenuProduct = menuProducts.stream()
                .reduce(
                        BigDecimal.ZERO,
                        (sum, menuProduct) -> sum.add(multiply(productPrice, menuProduct)),
                        BigDecimal::add
                );
        return price.compareTo(sumOfMenuProduct) > 0;
    }

    private BigDecimal multiply(Map<Long, BigDecimal> productPrice, MenuProduct menuProduct) {
        final BigDecimal price = productPrice.get(menuProduct.getProductId());

        return menuProduct.calculateTotal(price);
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
