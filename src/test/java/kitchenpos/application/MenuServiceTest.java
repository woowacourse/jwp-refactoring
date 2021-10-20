package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
    }

    @DisplayName("메뉴의 가격은 0 원 이상이어야 한다")
    @Test
    void createExceptionPriceUnderZero() {
    }

    @DisplayName("메뉴가 속한 메뉴 그룹이 존재해야 한다")
    @Test
    void createExceptionMenuGroup() {
    }

    @DisplayName("메뉴 상품 목록에서 (상품의 가격 * 메뉴 상품의 갯수) 의 합이 0 원 이상이어야 한다")
    @Test
    void createExceptionSum() {
    }
}
