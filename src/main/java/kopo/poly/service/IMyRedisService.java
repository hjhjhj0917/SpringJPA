package kopo.poly.service;

import kopo.poly.dto.RedisDTO;

public interface IMyRedisService {

    RedisDTO saveString(RedisDTO pDTO) throws Exception;
}
