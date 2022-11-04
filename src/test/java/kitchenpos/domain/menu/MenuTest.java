package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuTest {

    @Mock
    private MenuValidator menuValidator;

    @DisplayName("메뉴 생성자")
    @Nested
    class Constructor {

        @DisplayName("가격이 0보다 작다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceIsLessThan0() {
            // given
            MenuProduct menuProduct = new MenuProduct();
            MenuGroup menuGroup = new MenuGroup("메뉴 그룹");

            // when & then
            assertThatThrownBy(
                    () -> new Menu(
                            "메뉴",
                            new Price(BigDecimal.valueOf(-1)), menuGroup, List.of(menuProduct), menuValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
