package kitchenpos.repository;

import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("상품 레파지토리 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품_아이디를_통해_상품_목록을_조회한다() {
        // given
        final List<Product> product = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(3));
        final List<Long> productIds = product.stream()
                                             .map(Product::getId)
                                             .collect(Collectors.toList());

        // when
        final List<Product> actual = productRepository.findAllByIdIn(productIds);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual).containsAll(product);
        });
    }
}
