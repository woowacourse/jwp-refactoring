package kitchenpos.domain;

import java.math.BigDecimal;
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

    public Menu() {
    }

    public static Menu of(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                          final Map<Product, Long> productToQuantity) {
        final var sumOfMenuProductPrice = productToQuantity.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sumOfMenuProductPrice) == 1) {
            throw new IllegalArgumentException("메뉴 가격이 메뉴에 속한 상품 가격의 합보다 큽니다.");
        }
        final List<MenuProduct> menuProducts = productToQuantity.entrySet().stream()
                .map(entry -> new MenuProduct(null, null, entry.getKey().getId(), entry.getValue()))
                .collect(Collectors.toList());

        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    private Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격이 없거나 0보다 작습니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

}
