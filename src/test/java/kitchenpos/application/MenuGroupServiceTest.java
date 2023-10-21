package kitchenpos.application;

import kitchenpos.application.fixture.MenuGroupServiceFixture;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest extends MenuGroupServiceFixture {

    @Mock
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(저장된_메뉴_그룹);

        final MenuGroup actual = menuGroupService.create(생성할_메뉴_그룹);

        assertThat(actual).isEqualTo(저장된_메뉴_그룹);
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        given(menuGroupDao.findAll()).willReturn(저장된_메뉴_그룹_리스트);

        final List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).isEqualTo(저장된_메뉴_그룹_리스트);
    }
}
