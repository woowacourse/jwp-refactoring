package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql("/truncate.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private static Stream<String> wrongNameProvider() {
        return Stream.of("", null);
    }

    @DisplayName("메뉴그룹을 생성한다.")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup("단일 메뉴");
        MenuGroup savedGroup = menuGroupService.create(menuGroup);

        MenuGroup findMenuGroup = menuGroupDao.findById(savedGroup.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(findMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴그룹의 이름은 비어있을 수 없다.")
    @ParameterizedTest
    @MethodSource("wrongNameProvider")
    void createNameException(String wrongName) {
        MenuGroup menuGroup = new MenuGroup(wrongName);

        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%s : 올바르지 않은 이름입니다.", wrongName);
    }

    @DisplayName("메뉴그룹을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup1 = new MenuGroup("단일 메뉴");
        MenuGroup menuGroup2 = new MenuGroup("세트 메뉴");

        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        assertThat(menuGroupService.list()).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        menuGroupDao.deleteAll();
    }
}