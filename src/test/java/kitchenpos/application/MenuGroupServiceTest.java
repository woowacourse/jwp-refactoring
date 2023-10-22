//package kitchenpos.application;
//
//import static kitchenpos.fixtures.MenuGroupFixture.MENU_GROUP;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//import kitchenpos.domain.MenuGroup;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SuppressWarnings("NonAsciiCharacters")
//@DisplayNameGeneration(ReplaceUnderscores.class)
//@SpringBootTest
//class MenuGroupServiceTest {
//
//    @Autowired
//    private MenuGroupService menuGroupService;
//
//    @Test
//    void 메뉴_그룹을_생성한다() {
//        // given
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        // when
//        final MenuGroup actual = menuGroupService.create(menuGroup);
//
//        // then
//        final List<MenuGroup> menuGroups = menuGroupService.list();
//        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparator()
//                              .contains(actual);
//    }
//
//    @Test
//    void 메뉴_그룹에_대해_전체_조회한다() {
//        // given
//        final MenuGroup menuGroup = MENU_GROUP();
//        menuGroupService.create(menuGroup);
//
//        // when
//        final List<MenuGroup> menuGroups = menuGroupService.list();
//
//        // then
//        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
//                              .contains(menuGroup);
//    }
//}