//package kitchenpos.application;
//
//import kitchenpos.domain.MenuGroup;
//import kitchenpos.fixture.MenuGroupFixture;
//import kitchenpos.repository.MenuGroupRepository;
//import kitchenpos.ui.dto.request.MenuGroupRequest;
//import kitchenpos.ui.dto.response.MenuGroupResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("MenuGroupService 테스트")
//@SpringBootTest
//public class MenuGroupServiceTest {
//
//    @Autowired
//    private MenuGroupService menuGroupService;
//
//    @Autowired
//    private MenuGroupRepository menuGroupRepository;
//
//    private final MenuGroupFixture menuGroupFixture = new MenuGroupFixture();
//
//    @Test
//    @DisplayName("메뉴그룹 생성 테스트 - 성공")
//    public void createTest() throws Exception {
//        // given
//        MenuGroupRequest 메뉴그룹1_생성_요청 = menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹1");
//        MenuGroup expected = menuGroupFixture.메뉴그룹_생성("메뉴그룹1");
//
//        // when
//        MenuGroupResponse response = menuGroupService.create(메뉴그룹1_생성_요청);
//
//        // then
//        assertThat(menuGroupRepository.findById(response.getId())).isPresent();
//        assertThat(response).usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("모든 메뉴그룹 조회 테스트 - 성공")
//    public void listTest() throws Exception {
//        // given
//        MenuGroupRequest 메뉴그룹1_생성_요청 = menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹1");
//        MenuGroupRequest 메뉴그룹2_생성_요청 = menuGroupFixture.메뉴그룹_생성_요청("메뉴그룹2");
//
//
//        List<MenuGroupResponse> expected = menuGroupFixture.메뉴그룹_응답_리스트_생성(menuGroupService.create(메뉴그룹1_생성_요청), menuGroupService.create(메뉴그룹2_생성_요청));
//
//        // when
//        List<MenuGroupResponse> actual = menuGroupService.list();
//
//        // then
//        assertThat(actual).hasSize(expected.size());
//        assertThat(actual).usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(expected);
//    }
//}
