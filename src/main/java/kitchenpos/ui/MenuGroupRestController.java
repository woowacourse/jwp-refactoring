package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.menu.MenuGroupCreateRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody @Valid final MenuGroupCreateRequest request) {
        final MenuGroupResponse created = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list());
    }
}
