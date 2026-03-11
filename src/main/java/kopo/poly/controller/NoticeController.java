package kopo.poly.controller;

import jakarta.servlet.http.HttpSession;
import kopo.poly.service.INoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {

    private final INoticeService noticeService;

    @GetMapping(value = "noticeList")
    public String noticeList(HttpSession session, ModelMap model) throws Exception {

        log.info("{}.noticeList Start!", this.getClass().getName());

        session.setAttribute("SESSION_USER_ID", "USER01");


    }
}
