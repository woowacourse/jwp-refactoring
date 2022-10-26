package kitchenpos.support.fixture.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.application.dto.ProductSaveRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Product;

public class ProductDtoFixture {

    public static ProductSaveRequest 상품_생성_요청(Product product) {
        Name name = product.getName();
        Price price = product.getPrice();
        return new ProductSaveRequest(name.getValue(), price.getValue().intValue());
    }

    public static ProductResponse 상품_생성_응답(Product product) {
        Name name = product.getName();
        Price price = product.getPrice();
        return new ProductResponse(product.getId(), name, price);
    }
}
