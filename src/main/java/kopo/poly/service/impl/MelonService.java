package kopo.poly.service.impl;

import kopo.poly.dto.MelonDTO;
import kopo.poly.persistance.mongodb.impl.MelonMapper;
import kopo.poly.service.IMelonService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MelonService implements IMelonService {

    private final MelonMapper melonMapper;

    private List<MelonDTO> doCollect() throws Exception {

        log.info("{}.doCollect Start!", this.getClass().getName());

        List<MelonDTO> pList = new LinkedList<>();

        String url = "https://www.melon.com/chart/index.htm";

        Document doc = Jsoup.connect(url).get();

        Elements element = doc.select("div.service_list_song");

        for (Element songInfo : element.select("div.wrap_song_info")) {

            String song = CmmUtil.nvl(songInfo.select("div.ellipsis.rank01 a").text());
            String singer = CmmUtil.nvl(songInfo.select("div.ellipsis.rank02 a").eq(0).text());

            log.info("song: {}", song);
            log.info("singer: {}", singer);

            if ((!song.isEmpty()) && (!singer.isEmpty())) {

                MelonDTO pDTO = MelonDTO.builder().collectTime(DateUtil.getDateTime("yyyyMMddhhmmss"))
                        .song(song).singer(singer).build();
                pList.add(pDTO);
            }
        }

        log.info("{}.doCollect End!", this.getClass().getName());

        return pList;
    }

    @Override
    public int collectMelonSong() throws Exception {

        log.info("{}.collectMelonSong Start!", this.getClass().getName());

        int res;

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = this.doCollect();

        res = melonMapper.insertSong(rList, colNm);

        log.info("{}.collectMelonSong End!", this.getClass().getName());

        return res;
    }

    @Override
    public List<MelonDTO> getSongList() throws Exception {

        log.info("{}.getSongList Start!", this.getClass().getName());

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = melonMapper.getSongList(colNm);

        log.info("{}.getSongList End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSongCnt() throws Exception {

        log.info("{}.getSingerSongCnt Start!", this.getClass().getName());

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = melonMapper.getSingerSongCnt(colNm);

        log.info("{}.getSingerSongCnt End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSong(MelonDTO pDTO) throws Exception {

        log.info("{}.getSingerSong Start!", this.getClass().getName());

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = null;

        if (this.collectMelonSong() == 1) {
            rList = melonMapper.getSingerSong(colNm, pDTO);
        }

        log.info("{}.getSingerSong End!", this.getClass().getName());

        return rList;
    }

    @Override
    public int dropCollection() throws Exception {

        log.info("{}.dropCollection Start!", this.getClass().getName());

        int res;

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        res = melonMapper.dropCollection(colNm);

        log.info("{}.dropCollection End!", this.getClass().getName());

        return res;
    }

    @Override
    public List<MelonDTO> insertManyField() throws Exception {

        log.info("{}.insertManyField Start!", this.getClass().getName());

        List<MelonDTO> rList = null;

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        if (melonMapper.insertManyField(colNm, this.doCollect()) ==1) {
            rList = melonMapper.getSongList(colNm);
        }
        log.info("{}.insertManyField End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> updateField(MelonDTO pDTO) throws Exception {

        log.info("{}.updateField Start!", this.getClass().getName());

        List<MelonDTO> rList = null;

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        melonMapper.dropCollection(colNm);

        if (this.collectMelonSong() == 1) {
            if (melonMapper.updateField(colNm, pDTO) == 1) {

                rList = melonMapper.getUpdateSinger(colNm, pDTO);
            }
        }

        log.info("{}.updateField End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> updateAddField(MelonDTO pDTO) throws Exception {

        log.info("{}.updateAddField Start!", this.getClass().getName());

        List<MelonDTO> rList = null;

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        melonMapper.dropCollection(colNm);

        if (this.collectMelonSong() == 1) {
            if (melonMapper.updateAddField(colNm, pDTO) == 1) {

                rList = melonMapper.getSingerSongNickname(colNm, pDTO);
            }
        }

        log.info("{}.updateAddField End!", this.getClass().getName());

        return rList;
    }
}
