package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(
            ProductRepository productRepository,
            MenuGroupRepository menuGroupRepository
    ) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateMenuProducts(MenuProducts menuProducts, BigDecimal menuPrice) {
        List<Long> productIds = extractProductIds(menuProducts);
        List<Product> products = productRepository.findAllById(productIds);

        validateMenuProductSize(menuProducts, products);
        validateMenuProductPrice(menuProducts, menuPrice);
    }

    private List<Long> extractProductIds(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts()
                .stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    private void validateMenuProductSize(MenuProducts menuProducts, List<Product> products) {
        if (menuProducts.size() != products.size()) {
            throw new IllegalArgumentException("메뉴에 존재하지 않는 상품이 포함되어 있습니다.");
        }
    }

    private void validateMenuProductPrice(MenuProducts menuProducts, BigDecimal menuPrice) {
        double sumOfMenuProducts = menuProducts.getMenuProducts()
                .stream()
                .mapToDouble(this::calculateMenuProductPrice)
                .sum();

        if (menuPrice.doubleValue() > sumOfMenuProducts) {
            throw new IllegalArgumentException("메뉴 금액의 합계는 각 상품들의 합계보다 클 수 없습니다.");
        }
    }

    private Double calculateMenuProductPrice(MenuProduct menuProduct) {
        Product product = productRepository.getReferenceById(menuProduct.getProductId());
        double productPrice = product.getPrice().doubleValue();
        Long productQuantity = menuProduct.getQuantity();

        return productPrice * productQuantity;
    }

    public void validateMenuGroup(Long menuGroupId) {
        if (menuGroupRepository.existsById(menuGroupId)) {
            return;
        }

        throw new NoSuchElementException("ID에 해당하는 메뉴 그룹이 존재하지 않습니다.");
    }
}
