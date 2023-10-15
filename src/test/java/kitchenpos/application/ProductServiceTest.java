package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.ProductFixture.순살치킨_16000;
import static kitchenpos.fixture.ProductFixture.양념치킨_16000;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성할_수_있다() {
        // given
        Product 후라이드 = 후라이드_16000;

        // when
        Product created = productService.create(후라이드);

        // then
        assertThat(productDao.findById(created.getId())).isPresent();
    }

    @Test
    void 상품의_가격이_0원_미만이면_생성할_수_없다() {
        // given
        Product 가짜상품 = 상품("가짜상품", -100L);

        // when & then
        assertThatThrownBy(() -> productService.create(가짜상품))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹의_목록을_조회할_수_있다() {
        // given
        Product 후라이드 = productDao.save(후라이드_16000);
        Product 양념치킨 = productDao.save(양념치킨_16000);
        Product 순살치킨 = productDao.save(순살치킨_16000);

        // when
        List<Product> findList = productService.list();

        // then
        assertThat(findList).usingRecursiveComparison()
                .isEqualTo(List.of(후라이드, 양념치킨, 순살치킨));
    }
}
