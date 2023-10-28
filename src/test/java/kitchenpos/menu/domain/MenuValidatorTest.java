package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class MenuValidatorTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("금액의 총합이 메뉴의 가격보다 큰 경우 예외가 발생한다.")
    void throwsExceptionWhenAmountSumIsLargerThanMenuPrice() {
        // given
        final MenuValidator menuValidator = new MenuValidator(productRepository);
        final Product product = new Product("치킨", new BigDecimal("6000.00"));
        productRepository.save(product);
        final MenuProduct menuProduct = new MenuProduct(null, product.getId(), 2);

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(List.of(menuProduct), new BigDecimal("13000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 금액(가격 * 수량)의 합보다 클 수 없습니다.");
    }
}
