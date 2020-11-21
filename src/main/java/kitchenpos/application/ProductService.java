package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductCreateResponse;
import kitchenpos.dto.product.ProductFindAllResponses;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductCreateResponse create(final ProductCreateRequest productCreateRequest) {
        Product product = productCreateRequest.toEntity();
        final Money price = product.getPrice();

        validPriceIsNullOrPriceIsMinus(price);

        return new ProductCreateResponse(productRepository.save(product));
    }

    private void validPriceIsNullOrPriceIsMinus(Money price) {
        if (price.getValue() == null || price.isMinus()) {
            throw new IllegalArgumentException();
        }
    }

    public ProductFindAllResponses findAll() {
        return ProductFindAllResponses.from(productRepository.findAll());
    }
}
