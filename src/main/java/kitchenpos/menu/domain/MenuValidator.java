package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.DomainService;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@DomainService
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(final Long menuGroupId, final List<MenuProduct> menuProducts, final BigDecimal menuPrice) {
        validateMenuGroup(menuGroupId);
        final List<Product> products = findProducts(menuProducts);
        validateProducts(menuProducts, products);
        validatePrice(menuProducts, products, new MenuPrice(menuPrice));
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("존재하지 않는 MenuGroup 입니다.");
        }
    }

    private List<Product> findProducts(final List<MenuProduct> menuProducts) {
        return productRepository.findAllById(menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList()));
    }

    private void validateProducts(final List<MenuProduct> menuProducts, final List<Product> products) {
        if (products.size() != menuProducts.size()) {
            throw new IllegalArgumentException("존재하지 않는 Product가 존재합니다.");
        }
    }

    private void validatePrice(final List<MenuProduct> menuProducts, final List<Product> products,
                               final MenuPrice menuPrice) {
        final List<MenuProductAndProduct> menuProductAndProducts = MenuProductAndProduct.of(menuProducts, products);
        final BigDecimal sum = menuProductAndProducts.stream()
                .map(MenuProductAndProduct::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menuPrice.isExpensive(sum)) {
            throw new IllegalArgumentException("Menu 가격은 Product 가격의 합을 초과할 수 없습니다.");
        }
    }

    private static class MenuProductAndProduct {

        private final MenuProduct menuProduct;
        private final Product product;

        public MenuProductAndProduct(final MenuProduct menuProduct, final Product product) {
            this.menuProduct = menuProduct;
            this.product = product;
        }

        public static List<MenuProductAndProduct> of(final List<MenuProduct> menuProducts,
                                                     final List<Product> products) {
            List<MenuProductAndProduct> menuProductAndProducts = new ArrayList<>();
            for (int i = 0; i < menuProducts.size(); i++) {
                menuProductAndProducts.add(new MenuProductAndProduct(menuProducts.get(i), products.get(i)));
            }
            return menuProductAndProducts;
        }

        public BigDecimal calculatePrice() {
            return product.multiplyQuantity(menuProduct.getQuantity());
        }
    }
}
