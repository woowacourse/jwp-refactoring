package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(new MenuGroup());
        MenuGroup givenMenuGroup = new MenuGroup();

        // when, then
        assertThat(menuGroupService.create(givenMenuGroup)).isInstanceOf(MenuGroup.class);
    }

    @Test
    void 메뉴_그룹을_전체_조회한다() {
        // given
        given(menuGroupDao.findAll()).willReturn(new ArrayList<>());

        // when, then
        assertThat(menuGroupService.list()).isInstanceOf(List.class);
    }
}
