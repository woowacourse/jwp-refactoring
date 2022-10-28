package kitchenpos.domain;

import java.util.*;
import java.util.stream.Collectors;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts, List<Long> productId) {
        validateExistProductId(menuProducts, productId);
        this.menuProducts = comPressQuantity(menuProducts);
    }

    private void validateExistProductId(List<MenuProduct> menuProducts, List<Long> productId) {
        Set<Long> expectedProductId = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toSet());
        Set<Long> actualProductId = new HashSet<>(productId);
        if (!actualProductId.containsAll(expectedProductId)) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> comPressQuantity(List<MenuProduct> menuProducts) {
        Map<Long, Long> groupedQuantityByProduct = new HashMap<>();
        for (MenuProduct menuProduct : menuProducts) {
            Long quantity = groupedQuantityByProduct.computeIfAbsent(menuProduct.getProductId(), (id) -> 0L);
            groupedQuantityByProduct.put(menuProduct.getProductId(), quantity + menuProduct.getQuantity());
        }
        return groupedQuantityByProduct.entrySet().stream()
                .map(each -> new MenuProduct(each.getKey(), each.getValue()))
                .collect(Collectors.toUnmodifiableList());
    }

    public boolean isOverThanTotalPrice(Map<Long, Long> groupedPriceByProduct, long menuPrice) {
        return calculateTotalPrice(groupedPriceByProduct) < menuPrice;
    }

    private long calculateTotalPrice(Map<Long, Long> groupedPriceByProduct) {
        return this.menuProducts.stream()
                .mapToLong(each -> groupedPriceByProduct.get(each.getProductId()) * each.getQuantity())
                .sum();
    }

    public MenuProducts changeMenuId(Long menuId) {
        List<MenuProduct> changedMenuProducts = this.menuProducts.stream()
                .map(each -> each.changeMenuId(menuId))
                .collect(Collectors.toUnmodifiableList());
        List<Long> productIds = changedMenuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toUnmodifiableList());
        return new MenuProducts(changedMenuProducts, productIds);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
