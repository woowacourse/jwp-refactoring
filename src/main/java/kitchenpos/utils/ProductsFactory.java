package kitchenpos.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductsFactory {

    private final ProductRepository productRepository;

    public ProductsFactory(
        ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Map<Product, Long> generate(MenuCreateRequest menuCreateRequest) {
        Map<Product, Long> productsAndQuantity = new HashMap<>();
        List<MenuProductRequest> menuProductRequests = menuCreateRequest.getMenuProductRequests();

        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(()-> new IllegalArgumentException("Product를 찾을 수 없습니다."));

            productsAndQuantity.put(product, menuProductRequest.getQuantity());
        }

        return productsAndQuantity;
    }
}
