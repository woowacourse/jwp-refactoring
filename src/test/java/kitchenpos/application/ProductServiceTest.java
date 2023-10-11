package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService sut;

    @Autowired
    private ProductDao productDao;

    @Nested
    class 상품을_추가하는_경우 {

        @Test
        void 상품의_가격이_0원_미만인_경우_예외를_던진다() {
            // given
            Product product = 상품("피자", -1L);

            // expect
            assertThatThrownBy(() -> sut.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품의_가격이_0원_이상인_경우_정상적으로_등록된다() {
            // given
            Product product = 상품("무료 피자", 0L);

            // when
            Product savedProduct = sut.create(product);

            // then
            assertThat(productDao.findById(savedProduct.getId())).isPresent();
        }
    }

    @Test
    void 상품_목록을_조회한다() {
        // given
        Product pizza = productDao.save(상품("피자", 8900L));
        Product chicken = productDao.save(상품("치킨", 18000L));

        // when
        List<Product> result = sut.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(pizza, chicken));
    }
}
