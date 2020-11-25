package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.inmemory.InmemoryProductDao;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(new InmemoryProductDao());
    }

    @DisplayName("create: 이름과 가격을 입력 받아, 제품을 생성 요청 시, 입력 값을 기반으로  ID와 제품이 생성된다.")
    @Test
    void create() {
        Product 추가된새제품 = productService.create(createProduct("맛난 치킨", BigDecimal.valueOf(16_000)));

        assertAll(
                () -> assertThat(추가된새제품.getId()).isNotNull(),
                () -> assertThat(추가된새제품.getName()).isEqualTo("맛난 치킨"),
                () -> assertThat(추가된새제품.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(16_000))
        );
    }

    @DisplayName("create: 제품 생성 요청시, 입력 받은 가격이 음수라면, 제품 생성 시 예외가 발생한다.")
    @Test
    void create_throw_exception_if_price_is_negative() {
        Product 추가된새제품 = createProduct("맛난 치킨", BigDecimal.valueOf(-16_000));

        assertThatThrownBy(() -> productService.create(추가된새제품))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("create: 제품 생성 요청시, 입력 받은 가격이 null이라면, 제품 생성 시 예외가 발생한다.")
    @Test
    void create_throw_exception_if_price_is_null() {
        Product 가격이없는새제품 = createProduct("맛난 치킨", null);

        assertThatThrownBy(() -> productService.create(가격이없는새제품))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("list: 현재 저장 되어 있는 상품의 목록을 반환한다.")
    @Test
    void list() {
        productService.create(createProduct("후라이드 치킨", BigDecimal.valueOf(16_000)));
        productService.create(createProduct("오곡 치킨", BigDecimal.valueOf(16_000)));
        List<Product> 전체제품목록 = productService.list();

        assertThat(전체제품목록).hasSize(2);
    }
}