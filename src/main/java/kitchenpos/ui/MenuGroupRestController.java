package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.CreateMenuGroupDto;
import kitchenpos.ui.dto.request.CreateMenuGroupRequest;
import kitchenpos.ui.dto.response.CreateMenuGroupResponse;
import kitchenpos.ui.dto.response.ReadMenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {

    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<CreateMenuGroupResponse> create(@RequestBody final CreateMenuGroupRequest request) {
        final CreateMenuGroupDto createMenuGroupDto = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + createMenuGroupDto.getId());
        final CreateMenuGroupResponse response = new CreateMenuGroupResponse(createMenuGroupDto);

        return ResponseEntity.created(uri)
                             .body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<ReadMenuGroupResponse>> list() {
        final List<ReadMenuGroupResponse> responses = menuGroupService.list()
                                                                      .stream()
                                                                      .map(ReadMenuGroupResponse::new)
                                                                      .collect(Collectors.toList());

        return ResponseEntity.ok()
                             .body(responses);
    }
}
