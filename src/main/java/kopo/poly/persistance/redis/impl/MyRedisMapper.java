package kopo.poly.persistance.redis.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.redis.IMyRedisMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyRedisMapper implements IMyRedisMapper {

    private final RedisTemplate<String, Object> redisDB;

    private void deleteRedisKey(String redisKey) {
        if (Boolean.TRUE.equals(redisDB.hasKey(redisKey))) {
            redisDB.delete(redisKey);
            log.info("삭제 성공!");
        }
    }

    @Override
    public int saveStringJSON(String redisKey, RedisDTO pDTO) throws DataAccessException {

        log.info("{}.saveStringJSON Start!", this.getClass().getName());

        int res;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        this.deleteRedisKey(redisKey);

        redisDB.opsForValue().set(redisKey, pDTO);
        redisDB.expire(redisKey, 2, TimeUnit.DAYS);

        res = 1;

        log.info("{}.saveStringJSON End!", this.getClass().getName());

        return res;
    }

    @Override
    public RedisDTO getStringJSON(String redisKey) throws DataAccessException {

        log.info("{}.getStringJSON Start!", this.getClass().getName());

        log.info("String redisKey: {}", redisKey);

        RedisDTO rDTO = null;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        if (Boolean.TRUE.equals(redisDB.hasKey(redisKey))) {
            rDTO = (RedisDTO) redisDB.opsForValue().get(redisKey);
        }

        log.info("{}.getStringJSON End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public int saveList(String redisKey, List<RedisDTO> pList) throws DataAccessException {

        log.info("{}.saveList Start!", this.getClass().getName());

        int res;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        this.deleteRedisKey(redisKey);

        pList.forEach(dto -> {
            redisDB.opsForList().leftPush(redisKey, CmmUtil.nvl(dto.text()));
        });

        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info("{}.saveList End!", this.getClass().getName());

        return res;
    }

    @Override
    public List<String> getList(String redisKey) throws Exception {

        log.info("{}.getList Start!", this.getClass().getName());

        List<String> rList = null;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        if (Boolean.TRUE.equals(redisDB.hasKey(redisKey))) {
            rList = (List) redisDB.opsForList().range(redisKey, 0, -1);
        }

        log.info("{}.getList End!", this.getClass().getName());

        return rList;
    }

    @Override
    public int saveListJSON(String redisKey, List<RedisDTO> pList) throws DataAccessException {

        log.info("{}.saveListJSON Start!", this.getClass().getName());

        int res;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        this.deleteRedisKey(redisKey);

        pList.forEach(dto -> redisDB.opsForList().rightPush(redisKey, dto));

        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info("{}.saveListJSON End!", this.getClass().getName());

        return res;
    }

    @Override
    public List<RedisDTO> getListJSON(String redisKey) throws DataAccessException {

        log.info("{}.getListJSON Start!", this.getClass().getName());

        List<RedisDTO> rList = null;

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class));

        if (Boolean.TRUE.equals(redisDB.hasKey(redisKey))) {
            rList = (List) redisDB.opsForList().range(redisKey, 0, -1);
        }

        log.info("{}.getListJSON End!", this.getClass().getName());

        return rList;
    }
}
