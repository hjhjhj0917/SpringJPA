package kopo.poly.persistance.mongodb.impl;

import kopo.poly.dto.MongoDTO;
import kopo.poly.persistance.mongodb.AbstractMongoDBComon;
import kopo.poly.persistance.mongodb.IMongoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MongoMapper extends AbstractMongoDBComon implements IMongoMapper {

    private final MongoTemplate mongodb;

    @Override
    public int insertData(MongoDTO pDTO, String colNm) throws Exception {

        log.info("{}.insertData Start!", this.getClass().getName());



        log.info("{}.insertData End!", this.getClass().getName());

        return 0;
    }
}
