package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private static final MenuGroup standardMenuGroup = new MenuGroup();
    private static final List<MenuGroup> standardMenuGroups = new ArrayList<>();

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeAll
    static void beforeSetUp() {
        standardMenuGroup.setId(1L);
        standardMenuGroup.setName("메뉴이름");
        standardMenuGroups.add(standardMenuGroup);
    }

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
