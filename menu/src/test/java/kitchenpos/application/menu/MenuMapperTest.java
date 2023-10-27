package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.money.Money;
import kitchenpos.test.ServiceTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class MenuMapperTest {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void Menu로_변환한다() {
        // given
        Product product1 = productRepository.save(ProductFixture.상품("테오", 100000000L));
        Product product2 = productRepository.save(ProductFixture.상품("허브", 100000000L));
        MenuRequest menuRequest = new MenuRequest(null, "이름", BigDecimal.valueOf(1000), 1L, List.of(
                new MenuProductRequest(product1.getId(), 2),
                new MenuProductRequest(product2.getId(), 2)
        ));

        // when
        Menu menu = menuMapper.toMenu(menuRequest);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(menu.getName()).isEqualTo("이름");
            softly.assertThat(menu.getPrice()).isEqualTo(Money.valueOf(1000));
        });
    }
}
