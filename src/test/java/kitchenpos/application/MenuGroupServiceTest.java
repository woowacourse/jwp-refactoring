package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        //given
        MenuGroup 메뉴_그룹 = new MenuGroup();
        메뉴_그룹.setName("메뉴그룹");

        //when
        MenuGroup 생성된_메뉴그룹 = menuGroupService.create(메뉴_그룹);

        //then
        assertAll(
                () -> assertThat(생성된_메뉴그룹.getId()).isNotNull(),
                () -> assertThat(생성된_메뉴그룹.getName()).isEqualTo(메뉴_그룹.getName())
        );
    }
}
