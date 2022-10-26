package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.application.fixture.MenuGroupFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuGroupServiceTest {

    private final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private final MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);

    @BeforeEach
    void setUp() {
        FakeMenuGroupDao.deleteAll();
    }

    @Test
    void menuGroup을_생성한다() {
        MenuGroup 신메뉴 = 신메뉴();

        MenuGroup actual = menuGroupService.create(신메뉴);

        assertThat(actual.getName())
                .isEqualTo(신메뉴.getName());
    }

    @Test
    void menuGroup_list를_조회한다() {
        MenuGroup 한마리메뉴 = menuGroupDao.save(generateMenuGroup("한마리메뉴"));
        MenuGroup 두마리메뉴 = menuGroupDao.save(generateMenuGroup("두마리메뉴"));
        MenuGroup 순살파닭두마리메뉴 = menuGroupDao.save(generateMenuGroup("순살파닭두마리메뉴"));

        List<String> actual = menuGroupService.list()
                .stream()
                .map(MenuGroup::getName)
                .collect(toList());

        assertAll(() -> {
            assertThat(actual).hasSize(3);
            assertThat(actual)
                    .containsExactly(한마리메뉴.getName(), 두마리메뉴.getName(), 순살파닭두마리메뉴.getName());
        });
    }
}
