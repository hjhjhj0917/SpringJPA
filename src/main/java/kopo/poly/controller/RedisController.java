package kopo.poly.controller;

import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.RedisDTO;
import kopo.poly.service.IMyRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/redis/v1")
@RequiredArgsConstructor
@RestController
public class RedisController {

    private final IMyRedisService myRedisService;

    @PostMapping(value = "saveStringJSON")
    public ResponseEntity<CommonResponse<RedisDTO>> saveStringJSON(@RequestBody RedisDTO pDTO) throws Exception {

        log.info("{}.saveStringJSON Start!", this.getClass().getName());

        log.info("pDTO: {}", pDTO);

        RedisDTO rDTO = Optional.ofNullable(myRedisService.saveStringJSON(pDTO))
                .orElseGet(() -> RedisDTO.builder().build());

        log.info("{}.saveStringJSON End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO)
        );
    }

    @PostMapping(value = "saveList")
    public ResponseEntity<CommonResponse<List<String>>> saveList(@RequestBody List<RedisDTO> pList) throws Exception {

        log.info("{}.saveList Start!", this.getClass().getName());

        log.info("pList: {}", pList);

        List<String> rList = Optional.ofNullable(myRedisService.saveList(pList))
                .orElseGet(ArrayList::new);

        log.info("{}.saveList End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    @PostMapping(value = "saveListJSON")
    public ResponseEntity<CommonResponse<List<RedisDTO>>> saveListJSON(@RequestBody List<RedisDTO> pList) throws Exception {

        log.info("{}.saveListJSON Start!", this.getClass().getName());

        log.info("pList: {}", pList);

        List<RedisDTO> rList = Optional.ofNullable(myRedisService.saveListJSON(pList))
                .orElseGet(ArrayList::new);

        log.info("{}.saveListJSON End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }
}
