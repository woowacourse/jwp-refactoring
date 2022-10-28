package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.application.request.ProductRequest;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class ProductServiceTest {

    private ProductService sut;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        sut = new ProductService(productRepository);
    }

    @DisplayName("새로운 상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final ProductRequest request = new ProductRequest("후라이드", BigDecimal.valueOf(16000));

        // when
        final ProductResponse productResponse = sut.create(request);

        // then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getId()).isNotNull();
        final Product foundProduct = productRepository.findById(productResponse.getId()).get();
        assertThat(foundProduct)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(productResponse);
    }

    @DisplayName("상품 목록을 전체 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<ProductResponse> products = sut.list();

        // then
        assertThat(products)
                .hasSize(6)
                .extracting(ProductResponse::getName, product -> product.getPrice().longValue())
                .containsExactlyInAnyOrder(
                        tuple("후라이드", 16_000L),
                        tuple("양념치킨", 16_000L),
                        tuple("반반치킨", 16_000L),
                        tuple("통구이", 16_000L),
                        tuple("간장치킨", 17_000L),
                        tuple("순살치킨", 17_000L)
                );
    }
}
