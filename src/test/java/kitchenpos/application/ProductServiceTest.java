package kitchenpos.application;

import static kitchenpos.support.DomainFixture.뿌링클;
import static kitchenpos.support.DomainFixture.치즈볼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest // (includeFilters = @Filter(type = FilterType.ANNOTATION, value = Service.class))
@Sql(scripts = "classpath:truncate.sql")
class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @Autowired
    public ProductServiceTest(final ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productService = new ProductService(productRepository);
    }

    @DisplayName("상품 생성 테스트")
    @Nested
    class CreateTest {

        @Test
        void 상품을_생성하고_결과를_반환한다() {
            // given
            final var request = new ProductCreateRequest(
                    뿌링클.getName(),
                    뿌링클.getPrice().intValue()
            );

            // when
            final var created = productService.create(request);

            // then
            assertAll(
                    () -> assertThat(created.getId()).isNotNull(),
                    () -> assertThat(created.getName()).isEqualTo(뿌링클.getName()),
                    () -> assertThat(created.getPrice()).isEqualTo(뿌링클.getPrice())
            );
        }

        @Test
        void 상품_가격이_없는_경우_예외를_던진다() {
            // given
            final var request = new ProductCreateRequest(뿌링클.getName(), null);

            // when & then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    void 상품_목록을_조회한다() {
        // given
        productRepository.save(뿌링클);
        productRepository.save(치즈볼);

        // when
        final var found = productService.list();

        // then
        assertThat(found).hasSize(2);
    }
}
