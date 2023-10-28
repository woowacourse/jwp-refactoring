package kitchenpos.menu;

import kitchenpos.common.Price;
import kitchenpos.product.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validatePrice(final Menu menu, final List<MenuProduct> toSaveMenuProducts) {
        final List<Long> productIds = getProductIds(toSaveMenuProducts);

        final List<Product> products = productRepository.findAllById(productIds);
        validateExists(products, productIds);

        final Price sum = getSumOfPrice(toSaveMenuProducts, products);

        if (menu.hasPriceGreaterThan(sum)) {
            throw new IllegalArgumentException(String.format("메뉴의 가격은 상품의 총 가격의 합보다 비쌀 수 없습니다. 메뉴 가격 = %s, 총 상품 가격 = %s", menu.getPrice(), sum));
        }
    }

    private List<Long> getProductIds(final List<MenuProduct> toSaveMenuProducts) {
        return toSaveMenuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(toList());
    }

    private void validateExists(final List<Product> products, final List<Long> productIds) {
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException(String.format("상품 식별자 중에 없는 저장되어 있지 않은 식별자가 존재합니다. id = %s", productIds));
        }
    }

    private Price getSumOfPrice(final List<MenuProduct> menuProducts, final List<Product> products) {
        Price sum = Price.zero();

        for (int i = 0; i < products.size(); i++) {
            final MenuProduct menuProduct = menuProducts.get(i);
            final Product product = products.get(i);
            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        return sum;
    }
}
