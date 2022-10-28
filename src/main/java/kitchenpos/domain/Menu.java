package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

//    // product, quantity list를 받는다.
//    // product가 있는지에 대한 검증은 productService에서..
//    BigDecimal sum = BigDecimal.valueOf(menuProducts.entrySet()
//            .stream()
//            .mapToInt(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())).intValue())
//            .sum());
//
//
//        if (price.compareTo(sum) > 0) {
//        throw new IllegalArgumentException();
//    }

    public static Menu of(String name, Long price, MenuGroup menuGroup, Map<Product, Integer> productQuantity) {
        validateExistMenuGroup(menuGroup);
        validateExistProducts(productQuantity.keySet());
        return new Menu(name, validatePrice(price, productQuantity), menuGroup.getId(), null);
    }

    private static BigDecimal validatePrice(Long price, Map<Product, Integer> productQuantity) {
        if (Objects.isNull(price) || price < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal result = BigDecimal.valueOf(price);
        if (result.compareTo(totalPriceOfProducts(productQuantity)) > 0) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    private static void validateExistMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup.getId())) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateExistProducts(Set<Product> products) {
        if (products.stream()
                .anyMatch(product -> Objects.isNull(product.getId()))) {
            throw new IllegalArgumentException();
        }
    }

    private static BigDecimal totalPriceOfProducts(Map<Product, Integer> productQuantity) {

        return BigDecimal.valueOf(productQuantity.entrySet()
                .stream()
                .mapToInt(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())).intValue())
                .sum());
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
