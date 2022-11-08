package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Long menuGroupId, List<MenuProduct> menuProducts, BigDecimal price) {
        checkPriceIsNull(price);
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
        List<Product> products = getProducts(menuProducts);
        checkExistProducts(menuProducts, products);
        checkPriceIsNull(menuProducts, products, price);
    }

    private void checkPriceIsNull(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void checkExistProducts(List<MenuProduct> menuProducts, List<Product> products) {
        if (products.size() != menuProducts.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<Product> getProducts(List<MenuProduct> menuProducts) {
        List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
        return productRepository.findAllById(productIds);
    }

    private void checkPriceIsNull(List<MenuProduct> menuProducts, List<Product> products, BigDecimal price) {

        Map<Long, Long> quantities = menuProducts.stream()
                .collect(Collectors.toMap(MenuProduct::getProductId, MenuProduct::getQuantity));

        BigDecimal totalPrice = products.stream()
                .map(product -> product.getPrice().multiply(quantities.get(product.getId())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
