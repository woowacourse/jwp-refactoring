package kitchenpos.menu.domain.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.global.Price;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.model.MenuGroup;
import kitchenpos.menu.domain.model.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.model.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroupExist(menu.getMenuGroup());
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        List<Product> savedProducts = findProducts(menuProducts);
        validateProductsExist(menuProducts, savedProducts);
        validatePrice(menu, savedProducts);
    }

    private void validateMenuGroupExist(MenuGroup menuGroup) {
        if (!menuGroupRepository.existsById(menuGroup.getId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private List<Product> findProducts(List<MenuProduct> menuProducts) {
        List<Long> productIds = menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
        return productRepository.findAllById(productIds);
    }

    private void validateProductsExist(List<MenuProduct> menuProducts, List<Product> savedProducts) {
        if (menuProducts.size() != savedProducts.size()) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
    }

    private void validatePrice(Menu menu, List<Product> products) {
        Price sumOfMenuProductsPrice = calculateMenuProductsPrice(menu.getMenuProducts(), products);
        if (menu.getPrice().isGreaterThan(sumOfMenuProductsPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 상품의 가격 총 합보다 클 수 없습니다.");
        }
    }

    private Price calculateMenuProductsPrice(List<MenuProduct> menuProducts, List<Product> products) {
        Map<Long, Product> productIdToProduct = products.stream()
            .collect(Collectors.toMap(Product::getId, product -> product));
        return menuProducts.stream()
            .map(menuProduct ->
                productIdToProduct.get(menuProduct.getProductId())
                    .getPrice()
                    .multiply(menuProduct.getQuantity()))
            .reduce(Price.zero(), Price::add);
    }
}
