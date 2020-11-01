package kitchenpos.adapter.presentation.web;

import static kitchenpos.adapter.presentation.web.MenuGroupRestController.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;

@RequestMapping(API_MENU_GROUPS)
@RestController
public class MenuGroupRestController {
    public static final String API_MENU_GROUPS = "/api/menu-groups";

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody @Valid final MenuGroupRequest request) {
        Long menuGroupId = menuGroupService.create(request);
        final URI uri = URI.create(API_MENU_GROUPS + "/" + menuGroupId);
        return ResponseEntity.created(uri)
                .build()
                ;
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}
