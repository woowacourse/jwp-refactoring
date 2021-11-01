package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ServiceTest
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao mockMenuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        when(menuGroupService.create(any())).then(AdditionalAnswers.returnsFirstArg());
        MenuGroup menuGroup = createMenuGroup();
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        assertThat(savedMenuGroup).isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 그룹 목록을 반환한다.")
    @Test
    void list() {
        MenuGroup menuGroup = createMenuGroup();
        when(mockMenuGroupDao.findAll()).thenReturn(Collections.singletonList(menuGroup));
        List<MenuGroup> list = menuGroupService.list();
        assertAll(
                () -> assertThat(list).hasSize(1),
                () -> assertThat(list).contains(menuGroup)
        );
    }
}
