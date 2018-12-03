package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ClickButton;
import entity.Menu;
import entity.Message;
import entity.SubButton;
import entity.TextMessage;
import entity.ViewButton;
import service.WexinService;

/**
 * Servlet implementation class WexinServlet
 */
@WebServlet("/WexinServlet")
public class WexinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public WexinServlet() {
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("get");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if (WexinService.check(signature, timestamp, nonce)) {
			System.out.println("接入成功");
			PrintWriter out = response.getWriter();
			out.print(echostr);
			out.flush();
			out.close();
		} else {
			System.out.println("接入失败");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 获得输入流
		ServletInputStream is = request.getInputStream();
		// 输入流映射成Map
		Map<String,String> map = WexinService.parseXml(is);
		// 根据Map确定客户发送的文本类型，确定业务逻辑，返回想要的消息对象
		Message message = WexinService.getResp(map);
		// 将消息对象处理成Xml格式的响应
		String respXml = WexinService.mesParse2Xml(message);
		System.out.println(respXml);
		// 把Xml格式的响应发送给微信服务器
		PrintWriter out = response.getWriter();
		out.write(respXml);
		// 不等流满就发送
		out.flush();
		// 资源回收
		out.close();
		
	}

}
