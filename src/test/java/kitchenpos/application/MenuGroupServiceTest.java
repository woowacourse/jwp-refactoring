package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;

    public MenuGroupServiceTest(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 메뉴_그룹을_정상적으로_등록한다() {
        // given
        final MenuGroup 등록할_메뉴_그룹 = new MenuGroup("메뉴 그룹");

        // when
        final MenuGroup 등록된_메뉴_그룹 = menuGroupService.create(등록할_메뉴_그룹);

        // then
        assertThat(등록된_메뉴_그룹).isEqualTo(등록할_메뉴_그룹);
    }

    @Test
    void 메뉴_그룹_목록을_정상적으로_조회한다() {
        // given
        final MenuGroup 메뉴_그룹1 = menuGroupService.create(new MenuGroup("메뉴 그룹1"));
        final MenuGroup 메뉴_그룹2 = menuGroupService.create(new MenuGroup("메뉴 그룹2"));

        // when
        final List<MenuGroup> 메뉴_그룹들 = menuGroupService.list();

        // then
        assertThat(메뉴_그룹들).isEqualTo(List.of(메뉴_그룹1, 메뉴_그룹2));
    }
}
