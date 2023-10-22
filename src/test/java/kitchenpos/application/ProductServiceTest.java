package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.exception.InvalidRequestFormatException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    ProductService productService;

    @Nested
    class 상품_생성_테스트 {

        @Test
        void 상품을_정상_생성한다() {
            // given
            String productName = "name";
            BigDecimal price = new BigDecimal(10_000);
            Product product = Product.of(productName, price);
            em.persist(product);
            em.flush();
            em.clear();
            ProductRequest request = new ProductRequest(productName, price);

            // when
            ProductResponse response = productService.create(request);
            SoftAssertions.assertSoftly(softly -> {
                assertThat(response.getId()).isNotNull();
                assertThat(response.getName()).isEqualTo(productName);
                assertThat(response.getPrice()).isEqualTo(price);
            });
        }

        @Test
        void 가격이_없는_상품을_생성_시_예외를_반환한다() {
            // given
            ProductRequest request = new ProductRequest("name", null);

            // when, then
            assertThrows(InvalidRequestFormatException.class, () -> productService.create(request));
        }

        @Test
        void 음의_가격을_갖는_상품을_생성_시_예외를_반환한다() {
            // given
            ProductRequest request = new ProductRequest("name", new BigDecimal(-1));

            // when, then
            assertThrows(InvalidRequestFormatException.class, () -> productService.create(request));
        }
    }


    @Test
    void 상품을_전체_조회한다() {
        assertDoesNotThrow(() -> productService.list());
    }
}
