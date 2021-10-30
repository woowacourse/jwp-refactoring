package kitchenpos.application;

import kitchenpos.KitchenPosTestFixture;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceKitchenPosTest extends KitchenPosTestFixture {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = 메뉴_그룹을_저장한다(null, "추천 메뉴");
    }

    @DisplayName("메뉴 분류를 위한 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        MenuGroup savedMenuGroup = 메뉴_그룹을_저장한다(1L, menuGroup.getName());
        given(menuGroupDao.save(menuGroup)).willReturn(savedMenuGroup);

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result).isEqualTo(savedMenuGroup);
        verify(menuGroupDao, times(1)).save(menuGroup);
    }

    @DisplayName("메뉴 분류를 위한 그룹을 조회할 수 있다.")
    @Test
    void list() {
        // given
        MenuGroup savedMenuGroup1 = 메뉴_그룹을_저장한다(1L, menuGroup.getName());
        MenuGroup savedMenuGroup2 = 메뉴_그룹을_저장한다(2L, menuGroup.getName());

        // when
        given(menuGroupDao.findAll()).willReturn(
                Arrays.asList(savedMenuGroup1, savedMenuGroup2)
        );
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).containsExactly(savedMenuGroup1, savedMenuGroup2);
        verify(menuGroupDao, times(1)).findAll();
    }
}