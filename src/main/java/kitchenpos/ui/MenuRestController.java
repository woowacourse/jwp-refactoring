package kitchenpos.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.MenuService;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuCreateResponse;
import kitchenpos.dto.menu.MenuFindAllResponses;

@RestController
public class MenuRestController {
	private final MenuService menuService;

	public MenuRestController(final MenuService menuService) {
		this.menuService = menuService;
	}

	@PostMapping("/api/menus")
	public ResponseEntity<MenuCreateResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
		final MenuCreateResponse created = menuService.create(menuCreateRequest);
		final URI uri = URI.create("/api/menus/" + created.getId());
		return ResponseEntity.created(uri)
			.body(created);
	}

	@GetMapping("/api/menus")
	public ResponseEntity<MenuFindAllResponses> list() {
		return ResponseEntity.ok()
			.body(menuService.list());
	}
}
