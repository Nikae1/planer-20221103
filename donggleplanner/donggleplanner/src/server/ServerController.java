package server;

// 클라이언트 요청에 따른 서비스 분기
public class ServerController {

	public ServerController() {
		
	}
	
	public String controller(String clientData) {
		String accessResult = null;
		String serviceCode = (clientData.split("&")[0]).split("=")[1];
		
		if(serviceCode.equals("1")) {
			accessResult = new Auth().accessCtl(clientData) ? "1" : "0";
		}else if(serviceCode.equals("-1")) {
			new Auth().accessOut(clientData);
			accessResult = "로그아웃 완료";
		}else if(serviceCode.equals("9")) {
			accessResult = new TaskManager().getTodoDateCtl(clientData);
		}else if(serviceCode.equals("12")) {
			accessResult = new TaskManager().getToDoListCtl(clientData);
		}
		
		return accessResult;
	}
	
}
