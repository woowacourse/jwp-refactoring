package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Sql("/clean.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        MenuGroup expected = new MenuGroup("순살");

        // when
        MenuGroup actual = menuGroupService.create(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isPositive(),
            () -> assertThat(actual.getName()).isEqualTo("순살")

        );
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() {
        // given
        List<MenuGroup> expected = new ArrayList<>();
        expected.add(menuGroupDao.save(new MenuGroup("순살")));
        expected.add(menuGroupDao.save(new MenuGroup("뼈")));
        expected.add(menuGroupDao.save(new MenuGroup("뼈맛나는순살")));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
