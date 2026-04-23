package kopo.poly.persistance.redis.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.redis.IMyRedisMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyRRedisMapper implements IMyRedisMapper {

    private final RedisTemplate<String, Object> redisDB;

    private void deleteRedisKey(String redisKey) {
        if (Boolean.TRUE.equals(redisDB.hasKey(redisKey))) {
            redisDB.delete(redisKey);
            log.info("삭제 성공!");
        }
    }

    @Override
    public int saveString(String redisKey, RedisDTO pDTO) throws Exception {

        log.info("{}.saveString Start!", this.getClass().getName());

        int res;

        String saveData = CmmUtil.nvl(pDTO.text());

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        this.deleteRedisKey(redisKey);

        redisDB.opsForValue().set(redisKey, saveData);
        redisDB.expire(redisKey, 2, TimeUnit.DAYS);

        res = 1;

        log.info("{}.saveString End!", this.getClass().getName());

        return res;
    }

    @Override
    public RedisDTO getString(String redisKey) throws Exception {

        log.info("{}.getString Start!", this.getClass().getName());

        log.info("String redisKey: {}", redisKey);

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        RedisDTO rDTO = null;

        if (Boolean.TRUE.equals(redisDB.hasKey(redisKey))) {
            String res = (String) redisDB.opsForValue().get(redisKey);

            log.info("res: {}", res);

            rDTO = RedisDTO.builder().text(res).build();
        }

        log.info("{}.getString End!", this.getClass().getName());

        return rDTO;
    }
}
