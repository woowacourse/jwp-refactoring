package kitchenpos.dto.product;

import kitchenpos.domain.Product;

public class ProductCreateResponse {
	private Long id;
	private String name;
	private Long price;

	protected ProductCreateResponse() {
	}

	public ProductCreateResponse(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public ProductCreateResponse(Product product) {
		this(product.getId(), product.getName(), product.getPrice().getValue());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}
}
