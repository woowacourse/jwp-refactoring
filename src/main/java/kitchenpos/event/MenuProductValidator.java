package kitchenpos.event;

import kitchenpos.exception.NonExistentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.ProductQuantities;
import kitchenpos.menu.domain.ProductQuantity;
import kitchenpos.menu.ui.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuProductValidator {
    private final ProductRepository productRepository;

    public MenuProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(List<MenuProductRequest> menuProductRequests, Menu menu) {
        menu.validateTotalPrice(productQuantities(menuProductRequests).totalPrice());
    }

    private ProductQuantities productQuantities(List<MenuProductRequest> menuProductRequests) {
        List<ProductQuantity> productQuantities = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository
                    .findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new NonExistentException("상품을 찾을 수 없습니다."));
            productQuantities.add(new ProductQuantity(product, menuProductRequest.getQuantity()));
        }
        return new ProductQuantities(productQuantities);
    }
}
