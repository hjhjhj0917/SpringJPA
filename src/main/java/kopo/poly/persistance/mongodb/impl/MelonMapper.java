package kopo.poly.persistance.mongodb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import kopo.poly.dto.MelonDTO;
import kopo.poly.persistance.mongodb.AbstractMongoDBComon;
import kopo.poly.persistance.mongodb.IMelonMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.util.*;
import java.util.logging.Filter;

@Slf4j
@Component
@RequiredArgsConstructor
public class MelonMapper extends AbstractMongoDBComon implements IMelonMapper {

    private final MongoTemplate mongodb;

    @Override
    public int insertSong(List<MelonDTO> pList, String colNm) throws Exception {

        log.info("{}.insertSong Start!", this.getClass().getName());

        int res;

        if (super.createCollection(mongodb, colNm, "collectTime")) {
            log.info("{} 생성되었습니다.", colNm);
        }

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        for (MelonDTO pDTO : pList) {
            col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));
        }

        res = 1;

        log.info("{}.insertSong End!", this.getClass().getName());

        return res;
    }

    @Override
    public List<MelonDTO> getSongList(String colNm) throws Exception {

        log.info("{}.getSongList Start!", this.getClass().getName());

        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");

        projection.append("_id", 0);

        FindIterable<Document> rs = col.find(new Document()).projection(projection);

        for (Document doc : rs) {
            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));

            log.info("song: {}/ singer: {}", song, singer);

            MelonDTO rDTO = MelonDTO.builder().song(song).singer(singer).build();

            rList.add(rDTO);
        }

        log.info("{}.getSongList End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSongCnt(String colNm) throws MongoException {

        log.info("{}.getSingerSongCnt Start!", this.getClass().getName());

        List<MelonDTO> rList = new LinkedList<>();

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("singer", "$singer")
                                )
                                .append("COUNT(singer)", new Document()
                                        .append("$sum", 1)
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("singer", "$_id.singer")
                                .append("singerCnt", "$COUNT(singer)")
                                .append("_id", 0)
                        ),
                new Document()
                        .append("$sort", new Document()
                                .append("singerCnt", -1)
                        )
        );

        MongoCollection<Document> col = mongodb.getCollection(colNm);
        AggregateIterable<Document> rs = col.aggregate(pipeline).allowDiskUse(true);

        for (Document doc : rs) {

            String singer = doc.getString("singer");
            int singerCnt = doc.getInteger("singerCnt", 0);

            log.info("singer: {}/ singerCnt: {}", singer, singerCnt);

            MelonDTO rDTO = MelonDTO.builder().singer(singer).singerCnt(singerCnt).build();

            rList.add(rDTO);
        }

        log.info("{}.getSingerSongCnt End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSong(String colNm, MelonDTO pDTO) throws Exception {

        log.info("{}.getSingerSong Start!", this.getClass().getName());

        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        Document query = new Document();
        query.append("singer", CmmUtil.nvl(pDTO.singer()));

        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");
        projection.append("_id", 0);

        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {
            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));

            MelonDTO rDTO = MelonDTO.builder().song(song).singer(singer).build();

            rList.add(rDTO);
        }

        log.info("{}.getSingerSong End!", this.getClass().getName());

        return rList;
    }

    @Override
    public int dropCollection(String colNm) throws Exception {

        log.info("{}.dropCollection Start!", this.getClass().getName());

        int res = super.dropCollection(mongodb, colNm) ? 1 : 0;

        log.info("{}.dropCollection End!", this.getClass().getName());

        return res;
    }

    @Override
    public int insertManyField(String colNm, List<MelonDTO> pList) throws Exception {

        log.info("{}.insertManyField Start!", this.getClass().getName());

        int res;

        if (super.createCollection(mongodb, colNm, "collectTime")) {
            log.info("{} 생성되었습니다.", colNm);
        }

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        List<Document> list = new ArrayList<>();

        pList.parallelStream().forEach(melon ->
                list.add(new Document(new ObjectMapper().convertValue(melon, Map.class))));

        col.insertMany(list);

        res = 1;

        log.info("{}.insertManyField End!", this.getClass().getName());

        return res;
    }

    @Override
    public int updateField(String colNm, MelonDTO pDTO) throws Exception {

        log.info("{}.updateField Start!", this.getClass().getName());

        int res;

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        String singer = CmmUtil.nvl(pDTO.singer());
        String updateSinger = CmmUtil.nvl(pDTO.updateSinger());

        log.info("pDTO: {}", pDTO);

        Document update = new Document("$set", new Document("singer", updateSinger));

        col.updateMany(Filters.eq("singer", singer), update);

        res = 1;

        log.info("{}.updateField End!", this.getClass().getName());

        return res;
    }

    @Override
    public List<MelonDTO> getUpdateSinger(String colNm, MelonDTO pDTO) throws Exception {

        log.info("{}.getUpdateSinger Start!", this.getClass().getName());

        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        Document query = new Document();
        query.append("singer", CmmUtil.nvl(pDTO.updateSinger()));

        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");
        projection.append("_id", 0);

        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));

            log.info("song: {}, singer: {}", song, singer);

            MelonDTO rDTO = MelonDTO.builder().song(song).singer(singer).build();

            rList.add(rDTO);
        }

        log.info("{}.getUpdateSinger End!", this.getClass().getName());

        return rList;
    }
}
