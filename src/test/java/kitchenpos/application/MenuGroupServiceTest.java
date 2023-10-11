package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        final MenuGroup expect = new MenuGroup();
        when(menuGroupDao.save(any(MenuGroup.class)))
                .thenReturn(expect);

        // when
        final MenuGroup result = menuGroupService.create(new MenuGroup());

        // then
        assertThat(result).isEqualTo(expect);
    }

    @Test
    void 모든_메뉴_그룹을_반환한다() {
        // given
        final List<MenuGroup> expect = List.of();
        when(menuGroupDao.findAll())
                .thenReturn(expect);

        // when
        final List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).isEmpty();
    }
}