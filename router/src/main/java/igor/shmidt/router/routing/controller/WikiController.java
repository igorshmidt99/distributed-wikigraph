package igor.shmidt.router.routing.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface WikiController {

    ResponseEntity<List<String>> findWay(Map<String, String> pages);

}
