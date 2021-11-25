package kitchenpos.menu.domain;

import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Products;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class MenuValidatorImpl implements MenuValidator {

    private static final int COMPARE_RESULT_EQUAL = 0;

    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuValidatorImpl(
            MenuGroupRepository menuGroupRepository,
            MenuProductRepository menuProductRepository,
            ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void validate(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        BigDecimal price = menu.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < COMPARE_RESULT_EQUAL) {
            throw new IllegalArgumentException();
        }

        validateMenuPrice(menu);
    }

    private void validateMenuPrice(Menu menu) {
        Products products = new Products(productRepository.findAllById(menu.getProductIds()));
        BigDecimal productTotalPrice = products.sumTotalPrice(menu.getMenuProducts());
        if (isInvalidMenuPrice(menu, productTotalPrice)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isInvalidMenuPrice(Menu menu, BigDecimal productTotalPrice) {
        return menu.getPrice().compareTo(productTotalPrice) > COMPARE_RESULT_EQUAL;
    }
}
