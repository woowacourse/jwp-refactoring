//package kitchenpos.ui;
//
//import java.net.URI;
//import java.util.List;
//import kitchenpos.application.MenuService;
//import kitchenpos.domain.Menu;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/menus")
//public class MenuController {
//
//    private final MenuService menuService;
//
//    public MenuController(final MenuService menuService) {
//        this.menuService = menuService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Menu> create(@RequestBody final Menu menu) {
//        final Menu created = menuService.create(menu);
//        final URI uri = URI.create("/api/menus/" + created.getId());
//
//        return ResponseEntity.created(uri).body(created);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Menu>> list() {
//        return ResponseEntity.ok().body(menuService.list());
//    }
//}
