package kopo.poly.service.impl;

import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.UserINfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserINfoRepository userINfoRepository;

    @Override
    public UserInfoDTO getUserIdExists(@NonNull UserInfoDTO pDTO) throws Exception {

        log.info("{}.getUserIdExists Start!", this.getClass().getName());

        log.info("pDTO: {}", pDTO);

        String userId = CmmUtil.nvl(pDTO.userId());

        boolean exists = userINfoRepository.findByUserId(userId).isPresent();

        UserInfoDTO rDTO = UserInfoDTO.builder()
                .existsYn(exists ? "Y" : "N")
                .build();

        log.info("{}.getUserIdExists End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public int insertUserInfo(@NonNull UserInfoDTO pDTO) throws Exception {

        log.info("{}.insertUserInfo Start!", this.getClass().getName());

        log.info("pDTO: {}", pDTO);

        int res;

        String userId = CmmUtil.nvl(pDTO.userId());
        String userName = CmmUtil.nvl(pDTO.userName());
        String password = CmmUtil.nvl(pDTO.password());
        String email = CmmUtil.nvl(pDTO.email());
        String addr1 = CmmUtil.nvl(pDTO.addr1());
        String addr2 = CmmUtil.nvl(pDTO.addr2());

        Optional<UserInfoEntity> rEntity = userINfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {
            res = 2;
        } else {

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(userId).userName(userName)
                    .passsword(password)
                    .email(email)
                    .addr1(addr1).addr2(addr2)
                    .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();

            userINfoRepository.save(pEntity);

            res = userINfoRepository.findByUserId(userId).isPresent() ? 1 : 0;
        }

        log.info("{}.insertUserInfo End!", this.getClass().getName());

        return res;
    }

    @Override
    public int getUserLogin(@NonNull UserInfoDTO pDTO) throws Exception {

        log.info("{}.getUserLogin Start!", this.getClass().getName());

        String userId = CmmUtil.nvl(pDTO.userId());
        String password = CmmUtil.nvl(pDTO.password());

        log.info("userId: {}, password: {}", userId, password);

        boolean res = userINfoRepository.findByUserIdAndPasssword(userId, password).isPresent();

        log.info("{}.getUserLogin End!", this.getClass().getName());

        return res ? 1 : 0;
    }
}
