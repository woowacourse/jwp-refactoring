package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void price가_0원보다_작으면_예외가_발생한다() {
        //when, then
        Assertions.assertThatThrownBy(() -> productService.create("치킨", new BigDecimal(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 0보다 작거나 null일 수 없습니다.");
    }

    @Test
    void Product_를_조회할_수_있다() {
        //given
        final Product chicken = new Product("치킨", new BigDecimal(20000));
        final Product pizza = new Product("피자", new BigDecimal(20000));
        productRepository.save(chicken);
        productRepository.save(pizza);

        //when
        final List<Product> list = productService.list();

        //then
        assertThat(list).hasSize(2);
    }
}
