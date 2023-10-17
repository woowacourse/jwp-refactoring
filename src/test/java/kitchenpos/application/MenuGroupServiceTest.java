package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuGroupCommand;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        //given
        CreateMenuGroupCommand 커맨드 = new CreateMenuGroupCommand("메뉴그룹");

        //when
        MenuGroup 생성된_메뉴그룹 = menuGroupService.create(커맨드);

        //then
        assertAll(
                () -> assertThat(생성된_메뉴그룹.getId()).isNotNull(),
                () -> assertThat(생성된_메뉴그룹.getName()).isEqualTo(커맨드.getName())
        );
    }

    @Test
    void 메뉴_그룹_리스트를_조회할_수있다() {
        //given
        List<Long> 모든_그룹_아이디 = menuGroupDao.findAll().stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        //when
        List<MenuGroup> 메뉴_그룹_리스트 = menuGroupService.list();

        //then
        assertThat(메뉴_그룹_리스트).extracting(MenuGroup::getId)
                .containsAll(모든_그룹_아이디);
    }
}
