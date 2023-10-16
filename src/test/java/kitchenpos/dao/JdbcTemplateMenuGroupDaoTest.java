package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup  = MenuGroupFixture.음료();
    }

    @Test
    void 메뉴그룹을_등록한다() {
        // when
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // then
        assertThat(savedMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    void 메뉴그룹_id로_메뉴그룹을_조회한다() {
        // when
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        MenuGroup foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId()).get();

        // then
        assertThat(foundMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    void 전체_메뉴그룹_목록을_조회한다() {
        // given
        int originSize = menuGroupDao.findAll().size();

        // when
        menuGroupDao.save(menuGroup);
        List<MenuGroup> savedMenuGroups = menuGroupDao.findAll();

        // given
        assertThat(savedMenuGroups).hasSize(originSize + 1);
    }

    @Test
    void 전체메뉴_id로_메뉴그룹의_존재를_확인한다() {
        // given
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // when
        boolean existsById1 = menuGroupDao.existsById(savedMenuGroup.getId());
        boolean existsById2 = menuGroupDao.existsById(savedMenuGroup.getId() + 1);

        // given
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(existsById1).isTrue();
            softly.assertThat(existsById2).isFalse();
        });
    }
}
