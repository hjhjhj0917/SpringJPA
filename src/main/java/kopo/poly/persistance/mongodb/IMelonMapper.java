package kopo.poly.persistance.mongodb;

import kopo.poly.dto.MelonDTO;

import java.util.List;

public interface IMelonMapper {

    int insertSong(List<MelonDTO> pList, String colNm) throws Exception;

    List<MelonDTO> getSongList(String colNm) throws Exception;

    List<MelonDTO> getSingerSongCnt(String colNm) throws Exception;

    List<MelonDTO> getSingerSong(String colNm, MelonDTO pDTO) throws Exception;

    int dropCollection(String colNm) throws Exception;

    int insertManyField(String colNm, List<MelonDTO> pList) throws Exception;

    int updateField(String colNm, MelonDTO pDTO) throws Exception;

    List<MelonDTO> getUpdateSinger(String colNm, MelonDTO pDTO) throws Exception;

    int updateAddField(String colNm, MelonDTO pDTO) throws Exception;

    List<MelonDTO> getSingerSongNickname(String colNm, MelonDTO pDTO) throws Exception;

    // 가수의 List 구조의 Member 필드 추가하기
    int updateAddListField(String colNm, MelonDTO pDTO) throws Exception;

    // 가수의 노래 가져오기
    List<MelonDTO> getSingerSongMember(String colNm, MelonDTO pDTO) throws Exception;
}
