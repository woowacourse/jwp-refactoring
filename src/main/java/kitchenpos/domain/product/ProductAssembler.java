package kitchenpos.domain.product;

import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.repository.ProductRepository;
import kitchenpos.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductAssembler {
    private final ProductRepository productRepository;

    public ProductAssembler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Products createProducts(List<MenuCreateRequest.MenuProductDto> menuProductDtos) {
        ValidateUtil.validateNonNull(menuProductDtos);

        Map<Product, Long> productsAndQuantities = new HashMap<>();

        menuProductDtos
                .forEach(menuProductCreateRequest -> {
                    Long productId = menuProductCreateRequest.getProductId();
                    Product product =
                            productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
                    long quantity = menuProductCreateRequest.getQuantity();
                    productsAndQuantities.put(product, quantity);
                });

        return Products.from(productsAndQuantities);
    }
}
