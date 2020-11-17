package kitchenpos.application;

import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.dto.menu.ProductCreateRequest;
import kitchenpos.dto.menu.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private static final Long 상품_ID_1 = 1L;
    private static final Long 상품_ID_2 = 2L;
    private static final String 상품_이름_후라이드치킨 = "후라이드 치킨";
    private static final String 상품_이름_코카콜라 = "코카콜라";
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.00");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.00");

    private ProductService productService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }


    @DisplayName("Product 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        Product product = new Product(상품_ID_1, 상품_이름_후라이드치킨, 상품_가격_15000원);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductCreateRequest request = new ProductCreateRequest(상품_이름_후라이드치킨, 상품_가격_15000원);

        ProductResponse productResponse = productService.create(request);

        assertThat(productResponse.getId()).isEqualTo(product.getId());
        assertThat(productResponse.getName()).isEqualTo(product.getName());
        assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("예외 테스트 : Product 생성 중 0 미만의 가격이 전달될 경우, 예외가 발생한다.")
    @Test
    void createNegativePriceExceptionTest() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        ProductCreateRequest request = new ProductCreateRequest(상품_이름_후라이드치킨, invalidPrice);

        Assertions.assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 Product 가격이 입력되었습니다.");
    }

    @DisplayName("예외 테스트 : Product 생성 중 가격이 null일 경우, 예외가 발생한다.")
    @Test
    void createNullPriceExceptionTest() {
        ProductCreateRequest request = new ProductCreateRequest(상품_이름_후라이드치킨, null);
        Assertions.assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 Product 가격이 입력되었습니다.");
    }

    @DisplayName("예외 테스트 : Product 생성 중 이름이 유효하지 않은 경우, 예외가 발생한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void createNullOrEmptyNameExceptionTest(String invalidName) {
        ProductCreateRequest request = new ProductCreateRequest(invalidName, 상품_가격_15000원);
        Assertions.assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 Product 이름이 입력되었습니다.");
    }

    @DisplayName("Product 전체 목록을 조회 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        List<Product> products = Arrays.asList(
                new Product(상품_ID_1, 상품_이름_후라이드치킨, 상품_가격_15000원),
                new Product(상품_ID_2, 상품_이름_코카콜라, 상품_가격_1000원)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> foundProductResponses = productService.list();

        assertThat(foundProductResponses)
                .hasSize(2)
                .extracting("name")
                .containsOnly(상품_이름_후라이드치킨, 상품_이름_코카콜라);
    }
}
