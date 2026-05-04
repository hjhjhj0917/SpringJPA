package kopo.poly.persistance.redis;

import kopo.poly.dto.RedisDTO;

import java.util.List;
import java.util.Set;

public interface IMyRedisMapper {

    int saveStringJSON(String redisKey, RedisDTO pDTO) throws Exception;

    RedisDTO getStringJSON(String redisKey) throws Exception;

    int saveList(String redisKey, List<RedisDTO> pList) throws Exception;

    List<String> getList(String redisKey) throws Exception;

    int saveListJSON(String redisKey, List<RedisDTO> pList) throws Exception;

    List<RedisDTO> getListJSON(String redisKey) throws Exception;

    int saveHash(String redisKey, RedisDTO pDTO) throws Exception;

    RedisDTO getHash(String redisKey) throws Exception;

    int saveSetJSON(String redisKey, List<RedisDTO> pList) throws Exception;

    Set<RedisDTO> getSetJSON(String redisKey) throws Exception;
}
