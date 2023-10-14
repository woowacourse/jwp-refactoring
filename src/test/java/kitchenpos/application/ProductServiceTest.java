package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.fixture.ProductFixture.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class ProductServiceTest {
    
    @Autowired
    ProductService productService;
    
    @Autowired
    ProductDao productDao;
    
    @Test
    void 상품을_생성할_때_상품의_가격이_0미만이면_예외가_발생한다() {
        // given
        Product product = PRODUCT("짜장면", -1L);
        
        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품의 가격은 0원 이상이어야 합니다");
    }
    
    @Test
    void 상품을_생성한다() {
        // given
        Product product = PRODUCT("짜장면", 8000L);
        
        // when
        Product savedProduct = productService.create(product);
        
        // then
        assertThat(productDao.findById(savedProduct.getId())).isPresent();
    }
    
    @Test
    void list() {
        // given
        Product product1 = PRODUCT("짜장면", 8000L);
        Product savedProduct1 = productService.create(product1);
        Product product2 = PRODUCT("딤섬", 8000L);
        Product savedProduct2 = productService.create(product2);
        
        // when
        List<Product> expected = List.of(savedProduct1, savedProduct2);
        List<Product> actual = productService.list();
        
        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
