package server.beans;

public class MemberBean {
	
	private int fileIdx;
	private String accessCode;
	private String secretCode;
	private String userName;
	private String phoneNumber;
	private int activation;
	
	public int getFlieIdx() {
		return fileIdx;
	}
	public void setFlieIdx(int flieIdx) {
		this.fileIdx = flieIdx;
	}
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	public String getSecretCode() {
		return secretCode;
	}
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getActivation() {
		return activation;
	}
	public void setActivation(int activation) {
		this.activation = activation;
	}
	
}
