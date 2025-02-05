package igor.shmidt.router.routing.controller;

import igor.shmidt.router.routing.service.RouterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wiki")
@RequiredArgsConstructor
public class WikiControllerImpl implements WikiController {

    private final RouterService routerService;

    @Override
    @PostMapping("/way")
    public ResponseEntity<List<String>> findWay(@RequestBody Map<String, String> pages) {
        String reqId = routerService.sendToWorker(pages);
        List<String> ways = routerService.receiveWay(reqId);
        return ResponseEntity.ok(ways);
    }
}
