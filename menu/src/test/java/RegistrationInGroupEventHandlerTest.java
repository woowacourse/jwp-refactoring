import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRegisteredEvent;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.domain.RegistrationInGroupEventHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegistrationInGroupEventHandlerTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private RegistrationInGroupEventHandler registrationInGroupEventHandler;

    @DisplayName("존재하지 않는 메뉴 그룹의 경우 예외 처리")
    @Test
    void notFoundMenuGroup() {
        Mockito.when(menuGroupRepository.existsById(ArgumentMatchers.any())).thenReturn(false);

        assertThatIllegalArgumentException().isThrownBy(
            () -> registrationInGroupEventHandler.validate(
                new MenuRegisteredEvent(
                    new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
                        1L,
                        new MenuProducts(Collections.singletonList(
                            new MenuProduct(1L, 2L)
                        ))
                    )
                )
            ));
    }
}
