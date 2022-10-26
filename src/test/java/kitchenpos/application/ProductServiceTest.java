package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.application.fixture.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductServiceTest {

    private final ProductDao productDao;
    private final ProductService productService;

    ProductServiceTest() {
        this.productDao = new FakeProductDao();
        this.productService = new ProductService(productDao);
    }

    @BeforeEach
    void setUp() {
        FakeProductDao.deleteAll();
    }

    @Test
    void product를_생성한다() {
        Product 후라이드 = 후라이드();

        Product actual = productService.create(후라이드);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo(후라이드.getName());
            assertThat(actual.getPrice().compareTo(후라이드.getPrice())).isEqualTo(0);
        });
    }

    @Test
    void price가_null인_경우_예외를_던진다() {
        Product 후라이드 = generateProduct("후라이드", null);

        assertThatThrownBy(() -> productService.create(후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "price가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void price가_0미만인_경우_예외를_던진다(final int price) {
        Product 후라이드 = generateProduct("후라이드", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void product_list를_조회한다() {
        Product 후라이드 = productDao.save(generateProduct("후라이드"));
        Product 양념치킨 = productDao.save(generateProduct("양념치킨"));

        List<String> actual = productService.list()
                .stream()
                .map(Product::getName)
                .collect(toList());

        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual)
                    .containsExactly(후라이드.getName(), 양념치킨.getName());
        });
    }
}
