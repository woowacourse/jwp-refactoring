package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;

public class ProductFixture {

    public static Product createProduct(final String name) {
        return new Product(name, BigDecimal.valueOf(20_000));
    }

    public static Product createProduct(final int price) {
        return new Product("간장 치킨", BigDecimal.valueOf(price));
    }

    public static ProductRequest createProductRequest(final int price) {
        return new ProductRequest("간장 치킨", BigDecimal.valueOf(price));
    }

    public static WrapProductRequest createProductRequest(final String name, final int price) {
        return new WrapProductRequest(name, BigDecimal.valueOf(price));
    }

    public static class WrapProductRequest extends ProductRequest {

        public WrapProductRequest(final String name, final BigDecimal price) {
            super(name, price);
        }

        public Long id() {
            return super.getId();
        }

        public String name() {
            return super.getName();
        }

        public BigDecimal price() {
            return super.getPrice();
        }

        public double doublePrice() {
            return super.getPrice().doubleValue();
        }
    }

    public static class WrapProduct extends Product {

        public WrapProduct() {
        }

        public WrapProduct(final String name, final BigDecimal price) {
            super(name, price);
        }

        public Long id() {
            return super.getId();
        }

        public String name() {
            return super.getName();
        }

        public BigDecimal price() {
            return super.getPrice();
        }

        public double doublePrice() {
            return super.getPrice().doubleValue();
        }

        public int intPrice() {
            return super.getPrice().intValue();
        }
    }
}
