package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.ProductRepository;
import kitchenpos.dto.menu.ProductCreateRequest;
import kitchenpos.dto.menu.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        Product product = request.toProduct();
        Product savedProduct = productRepository.save(product);

        return new ProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findProductsByMenu(Menu menu) {
        return productRepository.findAllByMenu(menu)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
