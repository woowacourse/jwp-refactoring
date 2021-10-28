package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.exception.KitchenException;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<MenuProduct> convertToMenuProducts(MenuCreateRequest request) {
        return request.getMenuProducts().stream()
            .map(menuProductRequest -> {
                Product product = findProduct(products, menuProductRequest.getProductId());
                Long quantity = menuProductRequest.getQuantity();
                return new MenuProduct(product, quantity);
            }).collect(Collectors.toList());
    }

    private Product findProduct(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> productId.equals(product.getId()))
            .findAny()
            .orElseThrow(() -> new KitchenException("메뉴에 속한 상품이 존재하지 않습니다."));
    }
}
