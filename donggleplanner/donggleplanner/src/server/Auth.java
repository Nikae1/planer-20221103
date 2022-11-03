package server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;

// 로그인, 로그아웃, 히스토리(접속 기록) 서비스
public class Auth {
	
	public Auth() {
		
	}
	
	/* job : 로그인 
	 * 1. param : 1d, pw
	 * 2. id가 db에 존재하는지 여부 check
	 *    --> DAO가 MEMBERS 목록을 전달 --> 비교
	 *    2-1. true --> p3
	 *    2-2. false --> client
	 * 3. id와 pw를 db와 비교
	 *    3-1. true --> p4
	 *    3-2. false --> client
	 * 4. 접속 기록 (로그) 생성 
	 * 5. client에 결과 통보 */
	
	public boolean accessCtl(String clientData) {
		MemberBean user = (MemberBean)this.setBean(clientData);
		DataAccessObject dao = new DataAccessObject();
		
		user.setFlieIdx(0);
		ArrayList<MemberBean> memberList = dao.readDatabase(user.getFlieIdx());
		AccessHistoryBean historyBean;
		boolean accessReasult = false;
		
		if(this.compareAccessCode(user.getAccessCode(), memberList)) {
			if(this.isAuth(user, memberList)) {
				historyBean = new AccessHistoryBean();
				
				historyBean.setFileIdx(1);
				historyBean.setAccessCode(user.getAccessCode());
				historyBean.setDate(this.getDate(false));
				historyBean.setAccessType(1);
				
				accessReasult = dao.writeAccessHistory(historyBean);
			}
		}
		
		return accessReasult;
	}
	
	/* 로그아웃 타입*/
	public void accessOut(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		AccessHistoryBean historyBean = (AccessHistoryBean)this.setBean(clientData);
		
		historyBean.setFileIdx(1);
		historyBean.setDate(this.getDate(false));
		historyBean.setAccessType(-1);
		
		dao.writeAccessHistory(historyBean);
	}
	
	private String getDate(boolean isDate) {
		String pattern = (isDate) ? "yyyy. MM. dd." : "yyyy-MM-dd HH:mm:ss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)); 
	}
	
	/* 입력받은 AccessCode와 SecretCode를 분리 */
	private Object setBean(String clientData) {
		Object object = null;
		
		String[] splitData = clientData.split("&");
		switch(splitData[0].split("=")[1]) {
		case "-1":
			object = new AccessHistoryBean();
			((AccessHistoryBean)object).setAccessCode(splitData[1].split("=")[1]);
			break;
			
		case "1":
			object = new MemberBean();
			((MemberBean)object).setAccessCode(splitData[1].split("=")[1]);
			((MemberBean)object).setSecretCode(splitData[2].split("=")[1]);
			break;
		}
		
		return object;
	}
	
	
	/* AccessCode 존재 여부 판단 */
	private boolean compareAccessCode(String code, ArrayList<MemberBean> memberList) {
		boolean result = false;
		
		// MemberList에서 차례대로 꺼내서 Member에 넣겠다. 오른쪽이 큰 범위
		for(MemberBean memberInfo : memberList) {
			if(code.equals(memberInfo.getAccessCode())) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	/* AccessCode와 SecretCode의 비교 */
	private boolean isAuth(MemberBean user, ArrayList<MemberBean> memberList) {
		boolean result = false;
		for(MemberBean memberInfo : memberList) {
			if(user.getAccessCode().equals(memberInfo.getAccessCode())) {
				if(user.getSecretCode().equals(memberInfo.getSecretCode())) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	
	
}
