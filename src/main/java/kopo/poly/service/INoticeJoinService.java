package kopo.poly.service;

import kopo.poly.dto.NoticeDTO;

import java.util.List;

public interface INoticeJoinService {

    List<NoticeDTO> getNoticeListUsingJoinColumn();

    List<NoticeDTO> getNoticeListUsingNativeQuery();

    List<NoticeDTO> getNoticeListUsingJPQL();
}
