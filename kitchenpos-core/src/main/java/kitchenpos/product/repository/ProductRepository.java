package kitchenpos.product.repository;

import kitchenpos.product.Product;
import kitchenpos.product.persistence.ProductDataAccessor;
import kitchenpos.product.persistence.dto.ProductDataDto;
import kitchenpos.product.repository.converter.ProductConverter;
import kitchenpos.support.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository extends
        BaseRepository<Product, ProductDataDto, ProductDataAccessor, ProductConverter> {

    public ProductRepository(final ProductDataAccessor dataAccessor, final ProductConverter converter) {
        super(dataAccessor, converter);
    }
}
