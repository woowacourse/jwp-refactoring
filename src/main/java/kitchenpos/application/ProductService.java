package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.ProductCommand;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse create(final ProductCommand productCommand) {
        Product product = productRepository.save(productCommand.toEntity());
        return ProductResponse.from(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
