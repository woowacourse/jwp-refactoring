package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.ProductQuantities;
import kitchenpos.domain.menu.ProductQuantity;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.exception.NonExistentException;
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
