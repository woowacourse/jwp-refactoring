package kitchenpos.application;

import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.exception.NonExistentException;
import kitchenpos.ui.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("Menu 서비스 테스트")
public class MenuServiceTest extends ServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductService menuProductService;

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴 그룹을 찾을 수 없음.")
    @Test
    void createFailedWhenMenuGroupNotFound() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST("menu", BigDecimal.TEN, 1L,
                Arrays.asList(CREATE_MENU_PRODUCT_REQUEST(-1L, 1L))
        );

        // when
        given(menuGroupRepository.findById(menuRequest.getMenuGroupId()))
                .willThrow(NonExistentException.class);

        // then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NonExistentException.class);
        then(menuGroupRepository).should(times(1))
                .findById(menuRequest.getMenuGroupId());
        then(menuRepository).should(never())
                .save(any());
        then(menuProductService).should(never())
                .create(any(), any());
    }
}
