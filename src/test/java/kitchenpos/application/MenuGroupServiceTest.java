package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "/initialization.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 저장할 수 있다.")
    @Test
    void createSuccessTest() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TestMenuGroup");

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        MenuGroup findMenuGroup = menuGroupDao.findById(savedMenuGroup.getId()).get();

        assertThat(savedMenuGroup)
                .usingRecursiveComparison()
                .isEqualTo(findMenuGroup);
    }

    @DisplayName("메뉴 그릅을 조회할 수 있다.")
    @Test
    void listSuccessTest() {
        //given
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("TestMenuGroup1");
        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("TestMenuGroup2");

        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        //when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        //then
        assertThat(findMenuGroups).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(menuGroup1, menuGroup2));
    }
}
