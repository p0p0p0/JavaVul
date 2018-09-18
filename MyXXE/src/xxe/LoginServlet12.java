package xxe;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;


@WebServlet("/doLoginServlet12")
public class LoginServlet12 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String USERNAME = "admin";//�˺�
	private static final String PASSWORD = "admin";//����
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
        String result="";
		try {
			//DOM Read XML
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();     
	        
			/*����Ϊ�޸�����*/ 
		    //https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#Java
		
			//����DTDs (doctypes),�������Է�������xmlʵ�幥��
		    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //��ѡ
			
		    //������ܽ���DTDs,����ʹ���������������ͬʱ����
		    dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);		//��ֹ�ⲿʵ��POC 
		    dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);   //��ֹ����ʵ��POC
		    /*����Ϊ�޸�����*/	
		    
		    DocumentBuilder db = dbf.newDocumentBuilder();	    
			Document doc = db.parse(request.getInputStream());
			
			String username = getValueByTagName(doc,"username");
			String password = getValueByTagName(doc,"password");
			if(username.equals(USERNAME) && password.equals(PASSWORD)){
				result = String.format("<result><code>%d</code><msg>%s</msg></result>",1,username);
			}else{
				result = String.format("<result><code>%d</code><msg>%s</msg></result>",0,username);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			result = String.format("<result><code>%d</code><msg>%s</msg></result>",3,e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			result = String.format("<result><code>%d</code><msg>%s</msg></result>",3,e.getMessage());
		}
		response.setContentType("text/xml;charset=UTF-8");
		response.getWriter().append(result);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 
	 * @param doc �ĵ�
	 * @param tagName ��ǩ��
 	 * @return ��ǩֵ
	 */
	public static String getValueByTagName(Document doc, String tagName){  
        if(doc == null || tagName.equals(null)){  
            return "";  
        }  
        NodeList pl = doc.getElementsByTagName(tagName);  
        if(pl != null && pl.getLength() > 0){  
            return pl.item(0).getTextContent();  
        } 
        return "";
    }
}
