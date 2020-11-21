package kitchenpos.dto.product;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Product;

public class ProductFindAllResponses {
	private List<ProductFindAllResponse> productFindAllResponses;

	public ProductFindAllResponses() {
	}

	public ProductFindAllResponses(List<ProductFindAllResponse> productFindAllResponses) {
		this.productFindAllResponses = productFindAllResponses;
	}

	public static ProductFindAllResponses from(List<Product> products) {
		return products.stream()
			.map(ProductFindAllResponse::new)
			.collect(Collectors.collectingAndThen(Collectors.toList(), ProductFindAllResponses::new));
	}

	public List<ProductFindAllResponse> getProductFindAllResponses() {
		return productFindAllResponses;
	}
}
