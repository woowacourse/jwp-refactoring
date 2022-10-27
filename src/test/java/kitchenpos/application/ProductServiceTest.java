package kitchenpos.application;

import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends FakeSpringContext {

    private final ProductService productService = new ProductService(productDao);

    @DisplayName("create 메서드는")
    @Nested
    class create {

        @DisplayName("상품을 저장하고, 저장된 상품을 반환한다")
        @Test
        void saveProduct() {
            final var product = product("콜라", 1000);
            final var result = productService.create(product);

            assertAll(
                    () -> assertThat(result.getName()).isEqualTo(product.getName()),
                    () -> assertThat(result.getPrice().getPrice().compareTo(product.getPrice().getPrice())).isEqualTo(0)
            );
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class list {

        @DisplayName("등록된 모든 상품 목록을 조회해 반환한다")
        @Test
        void findAllProducts() {
            final var coke = product("콜라", 1000);
            final var rice = product("공기밥", 1500);

            productDao.save(coke);
            productDao.save(rice);

            final var result = productService.list();
            final var foundCoke = findProductInList(result, coke);
            final var foundRice = findProductInList(result, rice);

            assertAll(
                    () -> assertThat(foundCoke).isPresent(),
                    () -> assertThat(foundRice).isPresent()
            );
        }

        private Optional<Product> findProductInList(final List<Product> result, final Product target) {
            return result.stream()
                    .filter(product -> product.getName().equals(target.getName())
                            && product.getPrice().getPrice().compareTo(target.getPrice().getPrice()) == 0)
                    .findAny();
        }
    }
}
