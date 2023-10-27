package kitchenpos.menu.application;

import static kitchenpos.vo.Price.ZERO_PRICE;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.respository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.vo.Price;
import kitchenpos.vo.Quantity;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(
            final ProductRepository productRepository,
            final MenuGroupRepository menuGroupRepository
    ) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(final Menu menu) {
        validateMenuPrice(menu.getPrice(), calculateSumByMenuProducts(menu.getMenuProducts()));
        validateExistMenuGroupById(menu.getMenuGroupId());
    }

    private void validateExistMenuGroupById(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NoSuchElementException("존재하지 않는 menu group 입니다.");
        }
    }

    private void validateMenuPrice(
            final Price price,
            final Price sumByMenuProducts
    ) {
        if (price.isGreaterThan(sumByMenuProducts)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    private Price calculateSumByMenuProducts(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> calculateTotalPrice(findProductById(menuProduct.getProductId()),
                        menuProduct.getQuantity()))
                .reduce(ZERO_PRICE, Price::add);
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 product 입니다."));
    }

    private Price calculateTotalPrice(
            final Product product,
            final Quantity quantity
    ) {
        return product.multiplyPrice(quantity.getValue());
    }
}
