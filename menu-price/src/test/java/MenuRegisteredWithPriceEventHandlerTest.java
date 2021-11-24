import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.domain.MenuRegisteredWithPriceEventHandler;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRegisteredEvent;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuRegisteredWithPriceEventHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuRegisteredWithPriceEventHandler menuRegisteredWithPriceEventHandler;

    @DisplayName("존재하지 않는 제품일 경우 예외 처리")
    @Test
    void notFoundProduct() {
        Mockito.when(productRepository.findById(ArgumentMatchers.any()))
            .thenReturn(Optional.empty());

        assertThatIllegalArgumentException().isThrownBy(
            () -> menuRegisteredWithPriceEventHandler.validate(
                new MenuRegisteredEvent(
                    new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                        1L,
                        new MenuProducts(Collections.singletonList(
                            new MenuProduct(1L, 2L)
                        ))
                    )
                )));
    }

    @DisplayName("메뉴 가격이 상품 가격의 합보다 클 경우 예외 처리")
    @Test
    void priceOfMenuIsGreaterThanTotalPriceOfMenuProduct() {
        Mockito.when(productRepository.findById(ArgumentMatchers.any())).thenReturn(
            Optional.of(new Product("후라이드", BigDecimal.valueOf(7500)))
        );

        assertThatIllegalArgumentException().isThrownBy(
            () -> menuRegisteredWithPriceEventHandler.validate(
                new MenuRegisteredEvent(
                    new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                        1L,
                        new MenuProducts(Collections.singletonList(
                            new MenuProduct(1L, 2L)
                        ))
                    )
                )));
    }

    @DisplayName("메뉴 가격과 상품 가격의 합이 같을 경우 예외 처리 하지 않음")
    @Test
    void priceOfMenuEqualsTotalPriceOfMenuProduct() {
        Mockito.when(productRepository.findById(ArgumentMatchers.any())).thenReturn(
            Optional.of(new Product("후라이드", BigDecimal.valueOf(8000)))
        );

        assertThatCode(() -> menuRegisteredWithPriceEventHandler.validate(
            new MenuRegisteredEvent(
                new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                    1L,
                    new MenuProducts(Collections.singletonList(
                        new MenuProduct(1L, 2L)
                    ))
                )
            ))).doesNotThrowAnyException();
    }
}
