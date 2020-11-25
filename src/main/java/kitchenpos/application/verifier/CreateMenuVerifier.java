package kitchenpos.application.verifier;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.model.menu.Menu;
import kitchenpos.domain.model.menu.MenuProduct;
import kitchenpos.domain.model.menugroup.MenuGroupRepository;
import kitchenpos.domain.model.product.Product;
import kitchenpos.domain.model.product.ProductRepository;

@Component
public class CreateMenuVerifier {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public CreateMenuVerifier(MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu toMenu(String name, BigDecimal price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }

        if (price.compareTo(sumProductPrices(menuProducts)) > 0) {
            throw new IllegalArgumentException();
        }
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    private BigDecimal sumProductPrices(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(
                    product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }
}
