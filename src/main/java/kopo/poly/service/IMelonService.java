package kopo.poly.service;

import kopo.poly.dto.MelonDTO;

import java.util.List;

public interface IMelonService {

    int collectMelonSong() throws Exception;

    List<MelonDTO> getSongList() throws Exception;

    List<MelonDTO> getSingerSongCnt() throws Exception;

    List<MelonDTO> getSingerSong(MelonDTO pDTO) throws Exception;

    int dropCollection() throws Exception;

    List<MelonDTO> insertManyField() throws Exception;

    List<MelonDTO> updateField(MelonDTO pDTO) throws Exception;

    List<MelonDTO> updateAddField(MelonDTO pDTO) throws Exception;

    List<MelonDTO> updateAddListField(MelonDTO pDTO) throws Exception;

    // 가수 이름 수정 및 addData 필드 추가하기
    List<MelonDTO> updateFieldAndAddField(MelonDTO pDTO) throws Exception;

    // 노래 삭제하기
    List<MelonDTO> deleteDocument(MelonDTO pDTO) throws Exception;
}
