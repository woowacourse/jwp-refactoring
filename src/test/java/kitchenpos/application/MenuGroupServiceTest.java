package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.factory.KitchenPosFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private final MenuGroup standardMenuGroup = KitchenPosFactory.getStandardMenuGroup();
    private final List<MenuGroup> standardMenuGroups = KitchenPosFactory.getStandardMenuGroups();

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("그룹 생성 테스트")
    void create() {
        //given
        MenuGroup request = new MenuGroup();
        request.setName(standardMenuGroup.getName());

        given(menuGroupDao.save(request)).willReturn(standardMenuGroup);

        //when
        MenuGroup menuGroup = menuGroupService.create(request);

        //then
        assertThat(menuGroup).isNotNull();
        assertThat(menuGroup.getId()).isNotZero();
        assertThat(menuGroup).usingRecursiveComparison()
            .isEqualTo(standardMenuGroup);
    }

    @Test
    @DisplayName("리스트 반환 테스트")
    void list() {
        //given
        given(menuGroupDao.findAll()).willReturn(standardMenuGroups);

        //when
        List<MenuGroup> list = menuGroupService.list();

        //then
        assertThat(list).hasSize(standardMenuGroups.size()).
            containsExactly(standardMenuGroup);
    }
}
