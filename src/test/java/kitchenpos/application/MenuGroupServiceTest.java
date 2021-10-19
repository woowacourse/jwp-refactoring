package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;

@DisplayName("MenuGroupService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 이름을 통해 메뉴 그룹을 생성할 수 있다.")
    void create() {
        // given
        MenuGroup 추천메뉴 = new MenuGroup("추천 메뉴");
        MenuGroup expected = new MenuGroup(1L, "추천 메뉴");
        given(menuGroupDao.save(추천메뉴)).willReturn(expected);

        // when
        MenuGroup actual = menuGroupService.create(추천메뉴);

        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("저장되어 있는 모든 메뉴 그룹을 조회할 수 있다.")
    void list() {
        // given
        MenuGroup 추천메뉴 = new MenuGroup(1L, "추천 메뉴");
        MenuGroup 세트메뉴 = new MenuGroup(2L, "세트 메뉴");
        List<MenuGroup> expected = Arrays.asList(추천메뉴, 세트메뉴);
        given(menuGroupDao.findAll()).willReturn(expected);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertIterableEquals(expected, actual);
    }
}
