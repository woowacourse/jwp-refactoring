package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
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
        final MenuGroup savedMenuGroup = new MenuGroup();
        savedMenuGroup.setId(1L);
        savedMenuGroup.setName("신메뉴");
        when(menuGroupDao.save(any(MenuGroup.class)))
                .thenReturn(savedMenuGroup);

        // when
        final MenuGroup result = menuGroupService.create(new MenuGroup());
        final MenuGroup expect = new MenuGroup();
        expect.setId(1L);
        expect.setName("신메뉴");

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    void 모든_메뉴_그룹을_반환한다() {
        // given
        when(menuGroupDao.findAll())
                .thenReturn(Collections.emptyList());

        // when
        final List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).isEmpty();
    }
}