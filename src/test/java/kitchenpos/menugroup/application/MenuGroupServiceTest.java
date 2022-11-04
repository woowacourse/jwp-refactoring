package kitchenpos.menugroup.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.common.fixture.MenuGroupFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.dto.MenuGroupSaveRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuGroupServiceTest {

    private final MenuGroupDao menuGroupDao;
    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(final MenuGroupDao menuGroupDao, final MenuGroupService menuGroupService) {
        this.menuGroupDao = menuGroupDao;
        this.menuGroupService = menuGroupService;
    }

    @Test
    void menuGroup을_생성한다() {
        MenuGroupSaveRequest 한마리메뉴 = generateMenuGroupSaveRequest("한마리메뉴");

        MenuGroupResponse actual = menuGroupService.create(한마리메뉴);

        assertThat(actual.getName())
                .isEqualTo(한마리메뉴.getName());
    }

    @Test
    void menuGroup_list를_조회한다() {
        MenuGroup 한마리메뉴 = menuGroupDao.save(generateMenuGroup("한마리메뉴"));
        MenuGroup 두마리메뉴 = menuGroupDao.save(generateMenuGroup("두마리메뉴"));
        MenuGroup 순살파닭두마리메뉴 = menuGroupDao.save(generateMenuGroup("순살파닭두마리메뉴"));

        List<String> actual = menuGroupService.list()
                .stream()
                .map(MenuGroupResponse::getName)
                .collect(toList());

        assertAll(() -> {
            assertThat(actual).hasSize(3);
            assertThat(actual)
                    .containsExactly(한마리메뉴.getName(), 두마리메뉴.getName(), 순살파닭두마리메뉴.getName());
        });
    }
}
