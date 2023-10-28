package kitchenpos.menu.domain;

import kitchenpos.common.vo.Price;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuValidator {

    private ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateMenuPrice(final List<MenuProduct> menuProducts, final Price menuPrice) {
        final Map<Long, Product> products = findAllProducts(menuProducts);
        final Price totalPrice = calculateTotalPrice(menuProducts, products);

        if (menuPrice.compareTo(totalPrice)) {
            throw new InvalidMenuPriceException("메뉴 가격이 상품들의 가격 합보다 클 수 없습니다.");
        }
    }


    private Map<Long, Product> findAllProducts(final List<MenuProduct> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
                                                  .map(MenuProduct::getProductId)
                                                  .collect(Collectors.toList());

        return findProductsWithId(productIds);
    }

    private Map<Long, Product> findProductsWithId(final List<Long> productIds) {
        final Map<Long, Product> products = productRepository.findAllByIdIn(productIds).stream()
                                                             .collect(Collectors.toMap(Product::getId, product -> product));
        validateProducts(productIds, products);

        return products;
    }

    private void validateProducts(final List<Long> productIds, final Map<Long, Product> products) {
        if (products.size() != productIds.size()) {
            throw new NotFoundProductException("존재하지 않는 상품이 있습니다.");
        }
    }

    private Price calculateTotalPrice(final List<MenuProduct> menuProducts, final Map<Long, Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = products.get(menuProduct.getProductId());
            sum = sum.add(product.getPriceValue().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return new Price(sum);
    }
}
