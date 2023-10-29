package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.common.vo.Price;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateInit(final Menu menu) {
        validateInvalidMenuGroup(menu);
        validateInvalidProduct(menu);
        validateInvalidPrice(menu);
    }

    private void validateInvalidMenuGroup(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validateInvalidProduct(final Menu menu) {
        final List<Long> requiredProductIds = extractProductIds(menu.getMenuProducts());
        final Set<Long> savedProducts = findAllProductsByIds(requiredProductIds).stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
        for (Long requiredProductId : requiredProductIds) {
            if (!savedProducts.contains(requiredProductId)) {
                throw new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다.");
            }
        }
    }

    private List<Long> extractProductIds(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    private List<Product> findAllProductsByIds(final List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    private void validateInvalidPrice(final Menu menu) {
        Price menuPrice = menu.getPrice();
        Price maxPrice = new Price(BigDecimal.ZERO);

        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final List<Product> savedProducts = findAllProductsByIds(extractProductIds(menuProducts));

        for (MenuProduct menuProduct : menuProducts) {
            final Product savedProduct = findSavedProductsById(savedProducts, menuProduct.getProductId());
            maxPrice = maxPrice.add(savedProduct.getPrice().multiply(menuProduct.getQuantity()));
        }

        if (menuPrice.isHigherThan(maxPrice)) {
            throw new IllegalArgumentException("메뉴 가격 조정이 필요합니다.");
        }
    }

    private Product findSavedProductsById(final List<Product> savedProducts, final long productId) {
        return savedProducts.stream()
                .filter(each -> each.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
    }
}
