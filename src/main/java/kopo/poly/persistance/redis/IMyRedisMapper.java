package kopo.poly.persistance.redis;

import kopo.poly.dto.RedisDTO;

public interface IMyRedisMapper {

    int saveString(String redisKey, RedisDTO pDTO) throws Exception;

    RedisDTO getString(String redisKey) throws Exception;
}
