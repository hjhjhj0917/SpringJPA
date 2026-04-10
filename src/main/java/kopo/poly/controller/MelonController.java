package kopo.poly.controller;

import com.mongodb.internal.connection.CommandMessage;
import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.MelonDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.IMelonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
@RequestMapping(value = "/melon/v1")
@RequiredArgsConstructor
@RestController
public class MelonController {

    private final IMelonService melonService;

    @PostMapping(value = "collectMelonSong")
    public ResponseEntity<CommonResponse<MsgDTO>> collectMelonSong() throws Exception {

        log.info("{}.collectMelonSong Start!", this.getClass().getName());

        String msg;

        int res = melonService.collectMelonSong();

        if (res == 1) {
            msg = "멜론차트 수집 성공!";
        } else {
            msg = "멜론차트 수집 실패!";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info("{}.collectMelonSong End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto)
        );
    }

    @PostMapping(value = "getSongList")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> getSongList() throws Exception {

        log.info("{}.getSongList Start!", this.getClass().getName());

        List<MelonDTO> rLsit = Optional.ofNullable(melonService.getSongList()).orElseGet(ArrayList::new);

        log.info("{}.getSongList End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rLsit)
        );
    }

    @PostMapping(value = "getSingerSongCnt")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> getSingerSongCnt() throws Exception {

        log.info("{}.getSingerSongCnt Start!", this.getClass().getName());

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSongCnt()).orElseGet(ArrayList::new);

        log.info("{}.getSingerSongCnt End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    @PostMapping(value = "getSingerSong")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> getSingerSong(@RequestBody MelonDTO pDTO) throws Exception {

        log.info("{}.getSingerSong Start!", this.getClass().getName());

        log.info("pDTO: {}", pDTO);

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSong(pDTO)).orElseGet(ArrayList::new);

        log.info("{}.getSingerSong End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    @PostMapping(value = "dropCollection")
    public ResponseEntity<CommonResponse<MsgDTO>> dropCollection() throws Exception {

        log.info("{}.dropCollection Start!", this.getClass().getName());

        String msg;

        int res = melonService.dropCollection();

        if (res == 1) {
            msg = "멜론차트 삭제 성공";
        } else {
            msg = "멜론차트 삭제 실패";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info("{}.dropCollection End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto)
        );
    }

    @PostMapping(value = "insertManyField")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> insertManyField() throws Exception {

        log.info("{}.insertManyField Start!", this.getClass().getName());

        List<MelonDTO> rList = Optional.ofNullable(melonService.insertManyField())
                .orElseGet(ArrayList::new);

        log.info("{}.insertManyField End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    @PostMapping(value = "updateField")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> updateField(@RequestBody MelonDTO pDTO) throws Exception {

        log.info("{}.updateField Start!", this.getClass().getName());

        log.info("pDTO: {}", pDTO);

        List<MelonDTO> rList = Optional.ofNullable(melonService.updateField(pDTO))
                .orElseGet(ArrayList::new);

        log.info("{}.updateField End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }
}
