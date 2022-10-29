package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.request.ProductCreationRequest;

public class ProductCreationDto {

    private String name;
    private int price;

    private ProductCreationDto() {}

    private ProductCreationDto(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public static ProductCreationDto from(final ProductCreationRequest productCreationRequest) {
        return new ProductCreationDto(productCreationRequest.getName(), productCreationRequest.getPrice());
    }

    public static Product toEntity(final ProductCreationDto productCreationDto) {
        return new Product(productCreationDto.getName(), BigDecimal.valueOf(productCreationDto.getPrice()));
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductCreationDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
