package kitchenpos.menu.validator;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.exception.MenuNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.resitory.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository,
                         MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateCreation(Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validateMenuGroup(menuGroupId);
        validateMenuPrice(price, menuProducts);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupNotFoundException();
        }
    }

    private void validateMenuPrice(Price menuPrice, List<MenuProduct> menuProducts) {
        BigDecimal sum = calculateSumOfMenuProduct(menuProducts);
        if (menuPrice.isHigher(sum)) {
            throw new InvalidMenuPriceException();
        }
    }

    private BigDecimal calculateSumOfMenuProduct(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(MenuNotFoundException::new);
            sum = sum.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }
}
