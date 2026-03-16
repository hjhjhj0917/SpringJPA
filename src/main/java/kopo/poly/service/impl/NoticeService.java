package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.entity.NoticeEntity;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeDTO> getNoticeList() {

        log.info("{}.getNoticeList Start!", this.getClass().getName());

        List<NoticeEntity> rList = noticeRepository.findAllByOrderByNoticeSeqDesc();
        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });

        log.info("{}.getNoticeList End!", this.getClass().getName());

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) {

        log.info("{}.getNoticeInfo Start!", this.getClass().getName());

        if (type) {

            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            log.info("res: {}", res);
        }

        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(pDTO.noticeSeq());
        NoticeDTO rDTO = new ObjectMapper().convertValue(rEntity, NoticeDTO.class);

        log.info("{}.getNoticeInfo End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) {

        log.info("{}.updateNoticeInfo Start!", this.getClass().getName());

        Long noticeSeq = pDTO.noticeSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("noticeSeq: {}, title: {}, noticeYn: {}, contents: {}, userId: {}", noticeSeq, title, noticeYn, contents, userId);

        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(noticeSeq);

        NoticeEntity pEntity = NoticeEntity.builder()
                .noticeSeq(noticeSeq).title(title).noticeYn(noticeYn).contents(contents).userId(userId)
                .readCnt(rEntity.getReadCnt())
                .version(rEntity.getVersion())
                .build();

        noticeRepository.save(pEntity);

        log.info("{}.updateNoticeInfo End!", this.getClass().getName());
    }

    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info("{}.deleteNoticeInfo Start!", this.getClass().getName());

        Long noticeSeq = pDTO.noticeSeq();

        log.info("noticeSeq: {}", noticeSeq);

        noticeRepository.deleteById(noticeSeq);

        log.info("{}.deleteNoticeInfo End!", this.getClass().getName());
    }

    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info("{}.insertNoticeInfo Start!", this.getClass().getName());

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("pDTO: {}", pDTO);

        NoticeEntity pEntity = NoticeEntity.builder()
                .title(title).noticeYn(noticeYn).contents(contents).userId(userId).readCnt(0L)
                .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        noticeRepository.save(pEntity);

        log.info("{}.insertNoticeInfo End!", this.getClass().getName());
    }
}
