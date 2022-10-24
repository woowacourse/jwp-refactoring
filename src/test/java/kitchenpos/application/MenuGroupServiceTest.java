package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.두마리_메뉴_그룹;
import static kitchenpos.application.fixture.MenuGroupFixture.한마리_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("MenuGroup을 조회한다.")
    @Test
    void findAll() {
        // given
        menuGroupDao.save(한마리_메뉴_그룹());
        menuGroupDao.save(두마리_메뉴_그룹());

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }

    @DisplayName("MenuGroup을 정상적으로 등록한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup menuGroup = 한마리_메뉴_그룹();

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        List<MenuGroup> savedMenuGroups = menuGroupDao.findAll();
        assertAll(
                () -> assertThat(savedMenuGroups).hasSize(1),
                () -> assertThat(savedMenuGroup).extracting("name")
                        .isEqualTo(menuGroup.getName())
        );
    }

}
