package kitchenpos.dao;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product Dao 테스트")
@SpringBootTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private ProductDao productDao;

    private static Stream<Arguments> saveFailureWhenDbLimit() {
        return Stream.of(
                Arguments.of(IntStream.rangeClosed(1, 256)
                                .mapToObj(iter -> "x")
                                .collect(Collectors.joining()),
                        BigDecimal.valueOf(19000L)
                ),
                Arguments.of(null,
                        BigDecimal.valueOf(19000L)
                ),
                Arguments.of("검프",
                        new BigDecimal("123456789123456789.00")
                ),
                Arguments.of("검프",
                        null
                )
        );
    }

    @DisplayName("상품 저장 - 실패 - DB 제약사항")
    @CustomParameterizedTest
    @MethodSource("saveFailureWhenDbLimit")
    void saveFailureWhenDbLimit(String name, BigDecimal price) {
        //given
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        //when
        //then
        assertThatThrownBy(() -> productDao.save(product))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("상품 조회 - 성공 - id 기반 조회")
    @Test
    void findById() {
        //given
        //when
        final Optional<Product> foundProduct = productDao.findById(ProductFixture.후라이드치킨.getId());
        //then
        assertThat(foundProduct).isNotEmpty();
        assertThat(foundProduct.get().getName()).isEqualTo(ProductFixture.후라이드치킨.getName());
    }
}