package com.elcareates.legacy.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.elcareates.legacy.service.UserService;
import com.elcareates.legacy.vo.BbsVO;
import com.elcareates.legacy.vo.UserVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends HttpServlet{
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/main")
	public ModelAndView main(Locale locale, Model model){
		System.out.println("HomeControllerŬ���� main()");
		
		ModelAndView mav = null;
		mav = new ModelAndView();		
		mav.setViewName("/main");
		
		return mav;
	}
	
	@RequestMapping(value="/login")
	public ModelAndView login(Locale locale, Model model){
		System.out.println("HomeControllerŬ���� login()");
		
		ModelAndView mav = null;
		mav = new ModelAndView();		
		mav.setViewName("/login");
		
		return mav;
	}
	
	@RequestMapping(value="/loginAction")
	public ModelAndView loginAction(@ModelAttribute UserVO param, HttpServletRequest request
										, HttpServletResponse response, HttpSession session) throws IOException{
		System.out.println("HomeControllerŬ���� loginAction()");
		
		UserVO uvo = new UserVO();
		uvo = userService.loginUser(param);
		System.out.println("uvo >>> : " + uvo);
		ModelAndView mav = null;
		mav = new ModelAndView();
		PrintWriter out;
		if(uvo == null){
			try {
				mav.setViewName("main");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				out = response.getWriter();
				out.println("<script>");
				out.println("alert('ID/PW�� �ٽ� Ȯ�����ּ���');");
				out.println("history.back();");
				out.println("</script>");
				out.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			session.setAttribute("loginUser", uvo);
			mav.setViewName("main");
			return mav;
		}
		return mav;
	}
	
	@RequestMapping(value="/logoutAction.do")
	public ModelAndView logout(HttpSession session){
		session.removeAttribute("loginUser");
			
		ModelAndView mav = null;
		mav = new ModelAndView();		
		mav.setViewName("redirect:/index.jsp");
			
		return mav;
	}
	
	@RequestMapping(value="/join")
	public ModelAndView join(@ModelAttribute UserVO param){
		System.out.println("HomeControllerŬ���� join()");
		
		ModelAndView mav = null;
		mav = new ModelAndView();		
		mav.setViewName("/join");
		
		return mav;
	}
	
	@RequestMapping(value="/joinAction")
	public ModelAndView joinAction(@ModelAttribute UserVO param, HttpServletRequest request, HttpSession session){
		System.out.println("HomeControllerŬ���� joinAction()");
		
		ArrayList userList = null;
		userList = new ArrayList();
		
		System.out.println("param.getUserEmail >>> : " + param.getUseremail());
		System.out.println("param.getUserPW >>> : " + param.getUserpw());
		System.out.println("param.getUserName >>> : " + param.getUsername());
		System.out.println("param.getUserGender >>> : " + param.getUsergender());
		
		try{
			int result = userService.insertUser(param);
			
			if(result > 0){
				System.out.println("����� �Ϸ�Ǿ����ϴ�.");
			} else{
				System.out.println("��Ͽ� ������ �־� �Ϸ����� ���Ͽ����ϴ�.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		ModelAndView mav = null;
		mav = new ModelAndView();
		mav.setViewName( "redirect:/login.do");
		return mav;
		
		//StudyDAO sDao = null;
		//sDao = new StudyDAO();
		//int result = sDao.join(param);
		
		
		
//		// ���ڵ� �˻�
//		System.out.println("===���ڵ� �˻�===");
//		String originalStr = param.getUserID();  
//		String [] charSet = {"utf-8","euc-kr","ksc5601","iso-8859-1","x-windows-949"};
//		  
//		for (int i=0; i<charSet.length; i++) {
//		 for (int j=0; j<charSet.length; j++) {
//		  try {
//		   System.out.println("origin:[" + charSet[i] +", encoded:" + charSet[j] +"] = charset:" + new String(originalStr.getBytes(charSet[i]), charSet[j]));
//		  } catch (UnsupportedEncodingException e) {
//		   e.printStackTrace();
//		  }
//		 }
//		}
		
	}
	
	@RequestMapping(value="/bbs1List.do")
	public ModelAndView BBS1(Locale locale, Model model, HttpServletRequest request){
		System.out.println("HomeControllerŬ���� bbs1List()");
		 
		int rowcount = userService.rowCount();
		System.out.println("rowcount >>> "+rowcount);
		
		int pagecount = 0;
		if(rowcount%10 ==0){
			pagecount = rowcount/10; 
		}else{
			pagecount = rowcount/10 +1;
		}
		System.out.println("pagecount >>> "+pagecount);
		
		int pNum = Integer.parseInt(request.getParameter("pNum"));
		System.out.println("sNum >>> : " + pNum);
		
		List<BbsVO> bList = userService.selectAllBbs(pNum);
		System.out.println("bvo >>> : " + bList);
		
		ModelAndView mav = null;
		mav = new ModelAndView();		
		mav.addObject("pagecount", pagecount);
		mav.addObject("pNum", pNum);
		mav.addObject("bList", bList);
		mav.setViewName("/bbs1");
		return mav;
	}
	
	@RequestMapping(value="/bbsContent.do")
	public ModelAndView bbsContent(Locale locale, Model model, HttpServletRequest request){
		System.out.println("HomeControllerŬ���� bbsContent()");

		int bNum = Integer.parseInt(request.getParameter("bNum"));
		System.out.println("bNum >>> : " + bNum);
		
		List<BbsVO> bList = userService.selectBbs(bNum);
		System.out.println("bList >>> : " + bList);
		
		System.out.println(bList.get(0).getNo());
		System.out.println(bList.get(0).getWriter());
		System.out.println(bList.get(0).getSubject());
		System.out.println(bList.get(0).getContent());
		System.out.println(bList.get(0).getDate());
		
		ModelAndView mav = null;
		mav = new ModelAndView();		
		mav.addObject("bNum", bNum);
		mav.addObject("bList", bList);
		mav.setViewName("/content");
		return mav;
	}
	
	@RequestMapping(value="/write.do")
	public ModelAndView write(Locale locale, Model model, HttpServletRequest request){
		System.out.println("HomeControllerŬ���� write()");

		ModelAndView mav = null;
		mav = new ModelAndView();		
		mav.setViewName("/write");
		return mav;
	}
	
	@RequestMapping(value="/writeAction.do")
	public ModelAndView writeAction(@ModelAttribute BbsVO param, HttpServletRequest request){
		System.out.println("HomeControllerŬ���� writeAction()");

//		String writer = request.getParameter("writer");
//		String subject = request.getParameter("subject");
//		String content = request.getParameter("content");
//		System.out.println("writer >>> : " + writer);
//		System.out.println("subject >>> : " + subject);
//		System.out.println("content >>> : " + content);
		
		System.out.println(param.getWriter());
		System.out.println(param.getSubject());
		System.out.println(param.getContent());
		
		userService.insertBbs(param);
		
		ModelAndView mav = null;
		mav = new ModelAndView();
		mav.setViewName( "redirect:/bbs1List.do?pNum=1");
		return mav;
		
	}
	
	@RequestMapping(value="/update.do")
	public ModelAndView update(@ModelAttribute BbsVO param, HttpServletRequest request){
		System.out.println("HomeControllerŬ���� update()");
		
		System.out.println("param.getNo() >>> : " + param.getNo());
		System.out.println("param.getDate() >>> : " + param.getDate());
		System.out.println("param.getWriter() >>> : " + param.getWriter());
		System.out.println("param.getSubject() >>> : " + param.getSubject());
		System.out.println("param.getContent() >>> : " + param.getContent());
		
		userService.updateBbs(param);
		
		ModelAndView mav = null;
		mav = new ModelAndView();
		mav.setViewName( "redirect:/bbs1List.do?pNum=1");
		return mav;
	}
	
	@RequestMapping(value="/upload.do", method = RequestMethod.POST)
	public ModelAndView upload(@RequestParam MultipartFile upload, HttpServletRequest request
											  , HttpServletResponse response, HttpSession session){
		System.out.println("HomeControllerŬ���� upload()");
		
		OutputStream out = null;
        PrintWriter printWriter = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
 
        try{
        	String root_path = request.getSession().getServletContext().getRealPath("/");  
        	System.out.println("root_path >>> : " + root_path);
        	
            String attach_path = "resources/upload/";
            System.out.println("attach_path >>> : " + attach_path);
            
            String fileName = upload.getOriginalFilename();
            System.out.println("fileName >>> : " + fileName);
            
            String webPath = root_path + attach_path;
            String testPath = "E:/20.Study/study/src/main/webapp/resources/upload/";
            
            File file = new File(webPath + fileName);
            
            UUID uuid = UUID.randomUUID();
            
            if (fileName != null && !fileName.equals("")) {
                if (file.exists()) {
                    fileName = uuid + "_" + fileName;
                    
//                    file = new File(root_path+attach_path + fileName);
                    file = new File(webPath + fileName);
                }
            }
            
            byte[] bytes = upload.getBytes();
//            String uploadPath = root_path + attach_path + fileName;//������
//            String uploadPath = webPath + fileName;//������
//            System.out.println("uploadPath >>> : " + uploadPath);
            
              String uploadPath = "resources/upload/" + fileName;
 
            out = new FileOutputStream(file);
            out.write(bytes);
            String callback = request.getParameter("CKEditorFuncNum");
 
            printWriter = response.getWriter();
 
            printWriter.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction("
                    + callback
                    + ",'"
                    + uploadPath
                    + "','�̹����� ���ε� �Ͽ����ϴ�.'"
                    + ")</script>");
            printWriter.flush();
 
        }catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        ModelAndView mav = null;
		mav = new ModelAndView();
		return null;
    }
	
	@RequestMapping(value="/fileUpload.do")
	public ModelAndView fileUpload(MultipartHttpServletRequest multipartRequest, MultipartFile file, @ModelAttribute BbsVO param, HttpServletRequest request){
		System.out.println("HomeControllerŬ���� fileUpload()");
		
		List<MultipartFile> files = multipartRequest.getFiles("files");
		System.out.println("files >>> : " + files);

		for(int i=0 ; i<files.size() ; i++){
			
                // ���� �ߺ��� ó��
                String genId = UUID.randomUUID().toString();
                
                String path = "E:/20.Study/study/src/main/webapp/resources/upload/";
                
                // ���� ���ϸ�
                String originalfileName = files.get(i).getOriginalFilename();
                 
                // ����Ǵ� ���� �̸�
                String savePath = path + originalfileName; // ���� �� ���� ���
 
                long fileSize = files.get(i).getSize(); // ���� ������
 
                File uploadfile = new File(savePath);
                
                originalfileName = genId + "_" + originalfileName;
                        
                uploadfile = new File(path + originalfileName);
                
                // ���� ����
                try {
					files.get(i).transferTo(uploadfile);
					System.out.println("getOriginalFilename() >>> : " + files.get(i).getOriginalFilename());
					System.out.println("getSize() >>> : " + files.get(i).getSize()+ " byte");
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
 
		}
		
		
		ModelAndView mav = null;
		mav = new ModelAndView();
		return null;
	}
	
	@RequestMapping(value="/adMain.do")
	public ModelAndView adMain(@ModelAttribute BbsVO param, HttpServletRequest request){
		System.out.println("HomeControllerŬ���� adMain()");
		
		ModelAndView mav = null;
		mav = new ModelAndView();
		mav.setViewName( "admin_main");
		return mav;
	}
	
	@RequestMapping(value="/addBoard.do")
	public ModelAndView addBoard(HttpServletRequest request){
		System.out.println("HomeControllerŬ���� addBoard()");
		
		System.out.println(request.getParameter("bName"));
		
		
		ModelAndView mav = null;
		mav = new ModelAndView();
		mav.setViewName( "admin_main");
		return null;
	}
	
	@RequestMapping(value="/aTest.do")
	public String aTest(@ModelAttribute BbsVO param, HttpServletRequest request, ModelMap map){
		System.out.println("HomeControllerŬ���� aTest()");
		
		String m = "ModelMap";
		map.addAttribute("m", m);
		
		return "main";
	}
	
}