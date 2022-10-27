package kitchenpos.application.fixture.dto;

import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;

public class MenuGroupDtoFixture {

    public static MenuGroupRequest 메뉴그룹A_요청 = new MenuGroupRequest("메뉴그룹A");
    public static MenuGroupRequest 메뉴그룹B_요청 = new MenuGroupRequest("메뉴그룹B");
    public static MenuGroupRequest 메뉴그룹C_요청 = new MenuGroupRequest("메뉴그룹C");

    public static MenuGroupResponse 메뉴그룹A_응답 = new MenuGroupResponse(1L, "메뉴그룹A");
    public static MenuGroupResponse 메뉴그룹B_응답 = new MenuGroupResponse(2L, "메뉴그룹B");
    public static MenuGroupResponse 메뉴그룹C_응답 = new MenuGroupResponse(3L, "메뉴그룹C");
}
