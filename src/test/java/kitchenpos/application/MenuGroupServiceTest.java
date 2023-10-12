package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴그룹을_생성한다() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중국식 메뉴 그룹");

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedMenuGroup.getName())
                .isEqualTo(menuGroup.getName());
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        //given
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("중국식 메뉴 그룹");
        MenuGroup savedMenuGroup1 = menuGroupDao.save(menuGroup1);
        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("한식 메뉴 그룹");
        MenuGroup savedMenuGroup2 = menuGroupDao.save(menuGroup2);

        //when
        List<MenuGroup> queriedMenuGroups = menuGroupService.list();

        //then
        List<MenuGroup> savedMenuGroup = List.of(savedMenuGroup1, savedMenuGroup2);
        assertThat(queriedMenuGroups)
                .usingRecursiveComparison()
                .isEqualTo(savedMenuGroup);
    }
}
