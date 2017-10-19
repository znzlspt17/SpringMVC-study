package controllers.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import dao.NLNoticeDao;
import vo.Notice;

public class NoticeDetailController implements Controller {

	private NLNoticeDao noticeDao;

	public void setNoticeDao(NLNoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seq = request.getParameter("seq");
		Notice notice = noticeDao.getNotice(seq);

		ModelAndView mv = new ModelAndView("noticeDetail.jsp");
		mv.addObject("notice", notice);
		return mv;

	}

}
