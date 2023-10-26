package kitchenpos.product.domain.repository;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.converter.ProductConverter;
import kitchenpos.product.persistence.ProductDataAccessor;
import kitchenpos.product.persistence.dto.ProductDataDto;
import kitchenpos.support.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository extends
        BaseRepository<Product, ProductDataDto, ProductDataAccessor, ProductConverter> {

    public ProductRepository(final ProductDataAccessor dataAccessor, final ProductConverter converter) {
        super(dataAccessor, converter);
    }
}
