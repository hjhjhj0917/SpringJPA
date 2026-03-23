package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeFetchRepository;
import kopo.poly.repository.NoticeJoinRepository;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.NoticeSQLRepository;
import kopo.poly.repository.entity.*;
import kopo.poly.service.INoticeJoinService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeJoinService implements INoticeJoinService {

    private final NoticeJoinRepository noticeJoinRepository;
    private final NoticeSQLRepository noticeSQLRepository;
    private final NoticeFetchRepository noticeFetchRepository;
    private final JPAQueryFactory queryFactory;
    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeDTO> getNoticeListUsingJoinColumn() {

        log.info("{}.getNoticeListUsingJoinColumn Start!", this.getClass().getName());

        List<NoticeJoinEntity> rList = noticeJoinRepository.findAllByOrderByNoticeSeqDesc();

        List<NoticeDTO> list = new LinkedList<>();

        rList.forEach(rEntity -> {
            long noticeSeq = rEntity.getNoticeSeq();
            String noticeYn = CmmUtil.nvl(rEntity.getNoticeYn());
            String title = CmmUtil.nvl(rEntity.getTitle());
            long readCnt = rEntity.getReadCnt();
            String userName = CmmUtil.nvl(rEntity.getUserInfoEntity().getUserName());
            String regDt = CmmUtil.nvl(rEntity.getRegDt());

            log.info("noticeSeqe: ", noticeSeq);
            log.info("noticeYn: ", noticeYn);
            log.info("title: ", title);
            log.info("readCnt: ", readCnt);
            log.info("userName: ", userName);
            log.info("regDt: ", regDt);
            log.info("------------------------------");

            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(noticeSeq).noticeYn(noticeYn)
                    .title(title).readCnt(readCnt).userName(userName).regDt(regDt).build();

            list.add(rDTO);
        });

        log.info("{}.getNoticeListUsingJoinColumn End!", this.getClass().getName());

        return list;
    }

    @Override
    public List<NoticeDTO> getNoticeListUsingNativeQuery() {

        log.info("{}.getNoticeListUsingNativeQuery Start!", this.getClass().getName());

        List<NoticeSQLEntity> rList = noticeSQLRepository.getNoticeListUsigSQL();

        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<List<NoticeDTO>>() {
                });

        log.info("{}.getNoticeListUsingNativeQuery End!", this.getClass().getName());

        return nList;
    }

    @Override
    public List<NoticeDTO> getNoticeListUsingJPQL() {

        log.info("{}.getNoticeListUsingJPQL Start!", this.getClass().getName());

        List<NoticeFetchEntity> rList = noticeFetchRepository.getListFetchJoin();

        List<NoticeDTO> nList = new ArrayList<>();

        rList.forEach(e -> {
            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(e.getNoticeSeq()).title(e.getTitle()).noticeYn(e.getNoticeYn())
                    .readCnt(e.getReadCnt()).userId(e.getUserId())
                    .userName(e.getUserInfo().getUserName()).build();

            nList.add(rDTO);
        });

        log.info("{}.getNoticeListUsingJPQL End!", this.getClass().getName());

        return nList;
    }

    @Transactional
    @Override
    public List<NoticeDTO> getNoticeListForQueryDSL() {

        log.info("{}.getNoticeListForQueryDSL Start!", this.getClass().getName());

        QNoticeFetchEntity ne = QNoticeFetchEntity.noticeFetchEntity;
        QUserInfoEntity ue = QUserInfoEntity.userInfoEntity;

        List<NoticeFetchEntity> rList = queryFactory
                .selectFrom(ne)
                .join(ne.userInfo, ue)
                .orderBy(ne.noticeYn.desc(), ne.noticeSeq.desc())
                .fetch();

        List<NoticeDTO> nList = new ArrayList<>();

        rList.forEach(e -> {
            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(e.getNoticeSeq()).title(e.getTitle()).noticeYn(e.getNoticeYn())
                    .readCnt(e.getReadCnt()).userId(e.getUserId())
                    .userName(e.getUserInfo().getUserName()).build();

            nList.add(rDTO);
        });

        log.info("{}.getNoticeListForQueryDSL End!", this.getClass().getName());

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNOticeInfoForQueryDSL(NoticeDTO pDTO, boolean type) throws Exception {

        log.info("{}.getNOticeInfoForQueryDSL Start!", this.getClass().getName());

        if (type) {

            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            log.info("res: " + res);
        }

        QNoticeEntity ne = QNoticeEntity.noticeEntity;

        NoticeEntity rEntity = queryFactory
                .selectFrom(ne)
                .where(ne.noticeSeq.eq(pDTO.noticeSeq()))
                .fetchOne();

        NoticeDTO rDTO = NoticeDTO.builder().noticeSeq(rEntity.getNoticeSeq())
                .title(rEntity.getTitle())
                .noticeYn(rEntity.getNoticeYn())
                .regDt(rEntity.getRegDt())
                .userId(rEntity.getUserId())
                .readCnt(rEntity.getReadCnt())
                .contents(rEntity.getContents()).build();

        log.info("{}.getNOticeInfoForQueryDSL End!", this.getClass().getName());

        return rDTO;
    }
}
