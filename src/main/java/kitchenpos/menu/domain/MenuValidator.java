package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(final Long menuGroupId, final List<MenuProduct> menuProducts, final BigDecimal price) {
        validateMenuGroupExists(menuGroupId);
        List<Product> products = getProducts(menuProducts);
        validateProductExists(menuProducts, products);
        validatePrice(menuProducts, price, products);
    }

    private void validateMenuGroupExists(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private List<Product> getProducts(final List<MenuProduct> menuProducts) {
        List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .toList();
        return productRepository.findAllByIdIn(productIds);
    }

    private void validateProductExists(final List<MenuProduct> menuProducts, final List<Product> products) {
        if (products.size() != menuProducts.size()) {
            throw new IllegalArgumentException("제품이 존재하지 않습니다.");
        }
    }

    private void validatePrice(final List<MenuProduct> menuProducts, final BigDecimal price,
                               final List<Product> products) {
        Map<Long, BigDecimal> prices = products.stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        BigDecimal totalPrice = menuProducts.stream()
                .map(menuProduct -> BigDecimal.valueOf(menuProduct.getQuantity())
                        .multiply(prices.get(menuProduct.getProductId())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("잘못된 가격입니다.");
        }
    }
}
