package kitchenpos.product.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.common.domain.Money;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.dto.ProductUpdateRequest;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductCreateRequest request) {
        Money price = Money.from(request.getPrice());
        Product product = new Product(null, request.getName(), price);
        productRepository.save(product);
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
            .map(ProductResponse::from)
            .collect(toList());
    }

    public ProductResponse update(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new KitchenPosException("해당 상품이 없습니다. productId=" + productId));
        if (request.getPrice() != null) {
            product.changePrice(Money.from(request.getPrice()));
        }
        if (request.getName() != null) {
            product.changeName(request.getName());
        }
        return ProductResponse.from(product);
    }
}
