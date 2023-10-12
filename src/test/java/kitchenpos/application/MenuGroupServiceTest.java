package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupDao menuGroupDao;

    private List<MenuGroup> preSavedMenuGroups;

    private static Stream<List<MenuGroup>> should_return_menuGroup_list_when_request_list() {
        final MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("메뉴그룹1");

        final MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("메뉴그룹2");

        return Stream.of(
                List.of(),
                List.of(menuGroup1),
                List.of(menuGroup1, menuGroup2)
        );
    }

    @BeforeEach
    void setUp() {
        preSavedMenuGroups = menuGroupDao.findAll();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("모든 메뉴 그룹 목록을 조회할 수 있다.")
    void should_return_menuGroup_list_when_request_list(final List<MenuGroup> menuGroups) {
        // given
        menuGroups.forEach(menuGroup -> menuGroupDao.save(menuGroup));

        final List<MenuGroup> expect = preSavedMenuGroups;
        expect.addAll(menuGroups);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expect);
    }

    @Test
    void should_save_new_menuGroup_when_create() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴그룹");

        // when
        final MenuGroup expect = menuGroupService.create(menuGroup);

        // then
        final MenuGroup actual = menuGroupDao.findById(expect.getId()).get();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }
}
