package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.fixture.ProductFixture.createProduct;
import static kitchenpos.application.fixture.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 서비스")
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateProduct {
        private Product request;

        private Product subject() {
            return productService.create(request);
        }

        @Nested
        @DisplayName("이름과 가격이 주어지면")
        class WithNameAndPrice {
            @BeforeEach
            void setUp() {
                request = createProductRequest("치킨", BigDecimal.valueOf(1000));
            }

            @Test
            @DisplayName("상품을 생성한다")
            void createProduct() {
                given(productDao.save(any(Product.class))).willAnswer(i -> {
                    Product saved = i.getArgument(0, Product.class);
                    saved.setId(7L);
                    return saved;
                });
                Product result = subject();

                assertAll(
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result).isEqualToIgnoringGivenFields(request, "id")
                );
            }
        }

        @Nested
        @DisplayName("상품 가격이 없는 경우")
        class WithNullPrice {
            @BeforeEach
            void setUp() {
                request = createProductRequest("치킨", null);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateProduct.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("상품 가격이 음수인 경우")
        class WithNegativePrice {
            @BeforeEach
            void setUp() {
                request = createProductRequest("치킨", BigDecimal.valueOf(-1));
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateProduct.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("조회 메서드는")
    class FindProduct {
        private List<Product> subject() {
            return productService.list();
        }

        @Nested
        @DisplayName("상품이 저장되어 있으면")
        class WhenProductSaved {
            private List<Product> products;

            @BeforeEach
            void setUp() {
                products = Arrays.asList(
                        createProduct(1L, "치킨", BigDecimal.ONE),
                        createProduct(2L, "마요", BigDecimal.TEN),
                        createProduct(3L, "돈가", BigDecimal.valueOf(10000))
                );
                given(productDao.findAll()).willReturn(products);
            }

            @Test
            @DisplayName("전체 상품을 조회한다")
            void findAll() {
                List<Product> result = subject();

                assertThat(result).usingRecursiveComparison().isEqualTo(products);
            }
        }
    }
}