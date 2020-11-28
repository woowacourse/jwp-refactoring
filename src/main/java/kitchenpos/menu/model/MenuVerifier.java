package kitchenpos.menu.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kitchenpos.product.model.Product;
import kitchenpos.menuproduct.model.MenuProduct;

public class MenuVerifier {
    public static void validateMenuPrice(BigDecimal price, List<MenuProduct> menuProducts, List<Product> products) {
        Map<Product, Long> productsQuantity = products.stream()
            .collect(Collectors.toMap(
                product -> product, product -> menuProducts.stream()
                    .filter(menuProductCreateRequest -> menuProductCreateRequest.getProductId().equals(product.getId()))
                    .map(MenuProduct::getQuantity)
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 상품 번호입니다."))
            ));

        BigDecimal sum = productsQuantity.entrySet().stream()
            .map(entrySet -> entrySet.getKey().getPrice().multiply(BigDecimal.valueOf(entrySet.getValue())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품 가격의 합보다 작거나 같아야 합니다.");
        }
    }
}
