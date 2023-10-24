package kitchenpos.domain.repository;

import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static kitchenpos.fixture.ProductFixture.PRODUCT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void Product를_저장한다() {
        // given
        Product product = PRODUCT.후라이드_치킨();
        Product save = productRepository.save(product);

        // when
        Product actual = productRepository.findById(save.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNotNull();
            softly.assertThat(actual.getName()).isEqualTo(product.getName());
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(product.getPrice());
        });
    }
}
