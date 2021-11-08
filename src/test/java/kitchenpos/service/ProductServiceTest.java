package kitchenpos.service;

import kitchenpos.application.ProductService;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.ProductRestController;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
class ProductServiceTest extends ServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    private ProductRequest request;

    @BeforeEach
    void setUp() {
        String name = ProductFixture.create().getName();
        BigDecimal price = ProductFixture.create().getPrice();

        request = new ProductRequest(name, price);
    }

    @DisplayName("상품 생성 - 성공")
    @Test
    void create() {
        //given
        when(productRepository.save(any())).thenReturn(ProductFixture.create());
        //when
        ProductResponse productResponse = productService.create(request);
        //then
        assertThat(productResponse.getId()).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(request.getName());
    }

    @DisplayName("상품 조회")
    @Test
    void findAll() {
        //given
        when(productRepository.findAll()).thenReturn(Collections.singletonList(ProductFixture.create()));
        //when
        List<ProductResponse> products = productService.findAll();
        //then
        assertThat(products).hasSize(1);
    }
}
