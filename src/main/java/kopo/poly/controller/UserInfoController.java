package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Controller
public class UserInfoController {

    private final IUserInfoService userInfoService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {

        log.info("{}.userRegForm Start!", this.getClass().getName());
        log.info("{}.userRegForm End!", this.getClass().getName());

        return "user/userRegForm";
    }

    @GetMapping(value = "login")
    public String login() {

        log.info("{}.login Start!", this.getClass().getName());
        log.info("{}.login End!", this.getClass().getName());

        return "user/login";
    }

    @GetMapping(value = "loginSuccess")
    public String loginSuccess() {

        log.info("{}.loginSuccess Start!", this.getClass().getName());
        log.info("{}.loginSuccess End!", this.getClass().getName());

        return "user/loginSuccess";
    }

    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info("{}.getUserExists Start!", this.getClass().getName());

        String userId = CmmUtil.nvl(request.getParameter("userId"));

        log.info("userId: {}", userId);

        UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO)).orElseGet(() -> UserInfoDTO.builder().build());

        log.info("{}.getUserExists End!", this.getClass().getName());

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws Exception {

        log.info("{}.insertUserInfo Start!", this.getClass().getName());

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String password = CmmUtil.nvl(request.getParameter("password"));
        String email = CmmUtil.nvl(request.getParameter("email"));
        String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
        String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

        log.info("userId: {}, userName: {}, password: {}, email: {}, addr1: {}, addr2: {}",
                userId, userName, password, email, addr1, addr2);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .password(EncryptUtil.encHashSHA256(password))
                .email(EncryptUtil.encAES128BCBC(email))
                .addr1(addr1)
                .addr2(addr2)
                .regId(userId)
                .chgId(userId)
                .build();

        int res = userInfoService.insertUserInfo(pDTO);

        log.info("회원가입 결과(res): {}", res);

        if (res == 1) {
            msg = "회원가입되엇습니다.";
        } else if (res == 2) {
            msg = "이미 가입된 아이디입니다.";
        } else {
            msg = "오류로 인해 회원가입이 실패하였습니다.";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info("{}.insertUserInfo End!", this.getClass().getName());

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("{}.loginProc Start!", this.getClass().getName());

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        log.info("userId: {}, password: {}", userId, password);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .password(EncryptUtil.encHashSHA256(password)).build();

        int res = userInfoService.getUserLogin(pDTO);

        log.info("res: {}", res);

        if (res == 1) {
            msg = "로그인이 성공했습니다.";
            session.setAttribute("SS_USER_ID", userId);
        } else {
            msg = "아이디와 비밀번호가 일치하지 않습니다.";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info("{}.loginProc End!", this.getClass().getName());

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "logout")
    public MsgDTO logout(HttpSession session) {

        log.info("{}.logout Start!", this.getClass().getName());

        session.setAttribute("SS_USER_ID", "");
        session.removeAttribute("SS_USER_ID");

        MsgDTO dto = MsgDTO.builder().result(1).msg("로그아웃하였습니다.").build();

        log.info("{}.logout End!", this.getClass().getName());

        return dto;
    }

}
