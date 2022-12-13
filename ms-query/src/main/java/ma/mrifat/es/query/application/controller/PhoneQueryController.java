package ma.mrifat.es.query.application.controller;

import ma.mrifat.es.query.application.dto.PhoneResponse;
import lombok.extern.log4j.Log4j2;
import ma.mrifat.es.query.queries.PhoneQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class PhoneQueryController {

    @Autowired
    private PhoneQueryService queryService;

    @GetMapping("{aggregateId}")
    public ResponseEntity<PhoneResponse> getPhone(@PathVariable String aggregateId) {
        PhoneResponse result = queryService.handle(aggregateId);
        log.info("Get phone result: {}", result);
        return ResponseEntity.ok(result);
    }
}
