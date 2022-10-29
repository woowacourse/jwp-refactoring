package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;

public abstract class ProductFixture {

    public static Product product(String name) {
        return new Product(name, BigDecimal.valueOf(20_000));
    }

    public static Product product(int price) {
        return new Product("간장 치킨", BigDecimal.valueOf(price));
    }

    public static Product product(String name, int price) {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public static Product product(Long id, String name, int price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }

    public static ProductRequest productRequest(int price) {
        return new ProductRequest("간장 치킨", BigDecimal.valueOf(price));
    }

    public static WrapProductRequest productRequest(String name, int price) {
        return new WrapProductRequest(name, BigDecimal.valueOf(price));
    }

    public static final WrapProductRequest 후라이드_치킨 = productRequest("후라이드 치킨", 16_000);
    public static final WrapProductRequest 간장_치킨 = productRequest("간장 치킨", 17_000);
    public static final WrapProductRequest 양념_치킨 = productRequest("양념 치킨", 17_000);

    public static class WrapProductRequest extends ProductRequest {

        public WrapProductRequest(String name, BigDecimal price) {
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

        public WrapProduct(String name, BigDecimal price) {
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
