package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴그룹 등록에 성공한다.")
    void succeedInRegisteringMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        String menuGroupName = "즐겨찾는 메뉴";
        menuGroup.setName(menuGroupName);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertSoftly(softly -> {
                    softly.assertThat(savedMenuGroup.getId()).isNotNull();
                    softly.assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupName);
                }
        );
    }

    @Test
    @DisplayName("메뉴그룹 조회에 성공한다.")
    void succeedInSearchingMenuGroupList() {
        // given
        MenuGroup menuGroupA = new MenuGroup();
        menuGroupA.setName("즐겨찾는 메뉴");

        MenuGroup menuGroupB = new MenuGroup();
        menuGroupB.setName("자주 먹는 메뉴");

        // when
        menuGroupService.create(menuGroupA);
        menuGroupService.create(menuGroupB);

        // then
        assertThat(menuGroupService.list()).hasSize(2);
    }
}
