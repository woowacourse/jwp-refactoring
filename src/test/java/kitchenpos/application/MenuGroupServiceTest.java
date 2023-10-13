package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹_이름을_받아서_메뉴_그룹_정보를_등록할_수_있다() {
        //given
        MenuGroup menuGroupRequest = new MenuGroup();
        menuGroupRequest.setName("추천메뉴");

        MenuGroup savedMenuGroup = new MenuGroup();
        savedMenuGroup.setId(1L);
        savedMenuGroup.setName("추천메뉴");

        given(menuGroupDao.save(menuGroupRequest)).willReturn(savedMenuGroup);

        //when
        MenuGroup result = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("추천메뉴");
    }

    @Test
    void 등록된_전체_메뉴_그룹_정보를_조회할_수_있다() {
        //given
        MenuGroup savedMenuGroup1 = new MenuGroup();
        savedMenuGroup1.setId(1L);
        savedMenuGroup1.setName("추천메뉴1");

        MenuGroup savedMenuGroup2 = new MenuGroup();
        savedMenuGroup2.setId(2L);
        savedMenuGroup2.setName("추천메뉴2");

        given(menuGroupDao.findAll()).willReturn(List.of(savedMenuGroup1, savedMenuGroup2));

        //when
        List<MenuGroup> result = menuGroupService.list();

        //then
        assertThat(result).hasSize(2);
    }
}
