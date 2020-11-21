package kitchenpos.dto.product;

import kitchenpos.domain.Money;
import kitchenpos.domain.Product;

public class ProductCreateRequest {
	private Long id;
	private String name;
	private Long price;

	protected ProductCreateRequest() {
	}

	public ProductCreateRequest(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public Product toEntity() {
		return new Product(id, name, new Money(price));
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
