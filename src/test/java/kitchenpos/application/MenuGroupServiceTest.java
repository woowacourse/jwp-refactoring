package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
//
//    @Mock
//    MenuGroupDao menuGroupDao;
//
//    @InjectMocks
//    MenuGroupService menuGroupService;
//
//    @Test
//    void 메뉴그룹을_등록한다() {
//        // given
//        MenuGroup menuGroup = new MenuGroup();
//        menuGroup.setName("세트메뉴");
//        menuGroup.setId(1L);
//
//        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(menuGroup);
//
//        // when
//        MenuGroup result = menuGroupService.create(menuGroup);
//
//        // then
//        assertThat(result)
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(menuGroup);
//    }
//
//    @Test
//    void 모든_메뉴그룹_목록을_불러온다() {
//        // given
//        MenuGroup menuGroup = new MenuGroup();
//        menuGroup.setName("세트메뉴");
//        menuGroup.setId(1L);
//
//        given(menuGroupDao.findAll()).willReturn(List.of(menuGroup));
//
//        // when
//        List<MenuGroup> menuGroups = menuGroupService.list();
//
//        // then
//        assertThat(menuGroups.get(0))
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(menuGroup);
//    }


}
