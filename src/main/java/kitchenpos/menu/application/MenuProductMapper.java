package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductQuantityDto;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.vo.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductMapper {

    private final ProductRepository productRepository;

    public MenuProductMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MenuProducts toMenuProducts(List<MenuProductQuantityDto> menuProductQuantities) {
        List<MenuProduct> menuProducts = convertToMenuProducts(menuProductQuantities);
        return new MenuProducts(menuProducts);
    }

    private List<MenuProduct> convertToMenuProducts(List<MenuProductQuantityDto> menuProductQuantities) {
        return menuProductQuantities.stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductQuantityDto menuProductQuantity) {
        Product product = getProduct(menuProductQuantity);
        return new MenuProduct(
                product.getId(),
                product.getName(),
                product.getPrice(),
                menuProductQuantity.getQuantity()
        );
    }

    private Product getProduct(MenuProductQuantityDto menuProductQuantity) {
        return productRepository.findById(menuProductQuantity.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));
    }
}
