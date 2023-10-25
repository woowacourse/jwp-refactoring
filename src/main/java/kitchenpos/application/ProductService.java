package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateProductDto;
import kitchenpos.application.dto.ReadProductDto;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.ui.dto.request.CreateProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateProductDto create(final CreateProductRequest request) {
        final Product persistProduct = productRepository.save(new Product(request.getName(), request.getPrice()));

        return new CreateProductDto(persistProduct);
    }

    public List<ReadProductDto> list() {
        return productRepository.findAll()
                                .stream()
                                .map(ReadProductDto::new)
                                .collect(Collectors.toList());
    }
}
