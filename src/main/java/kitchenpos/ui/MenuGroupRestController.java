package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(
        @Valid @RequestBody final MenuGroupCreateRequest menuGroupCreateRequest
    ) {
        final MenuGroup menuGroup = menuGroupService.create(menuGroupCreateRequest.toRequestEntity());
        final MenuGroupResponse created = MenuGroupResponse.from(menuGroup);
        final URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());

        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        final List<MenuGroup> list = menuGroupService.list();
        final List<MenuGroupResponse> responses = list.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(responses);
    }
}
