package server;

import java.util.ArrayList;

import server.beans.TodoBean;

public class TaskManager {

	public TaskManager() {
		
	}
	
	/* 등록된 할 일 리스트 가져오기 */
	public String getTodoDateCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		
		TodoBean todo = (TodoBean)this.setBean(clientData);
		todo.setFileIdx(2);
		
		return this.convertServerData(dao.gettoDoList(todo));
	}
	
	public String getToDoListCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		
		return this.convertServerData2(dao.getTodo((TodoBean)this.setBean(clientData)));
	}
	
	private Object setBean(String clientData) {
		Object object = null;
		String[] splitData = clientData.split("&");
		
		switch(splitData[0].split("=")[1]) {
		
		case "9":
			object = new TodoBean();
			((TodoBean)object).setAccessCode(splitData[1].split("=")[1]);
			((TodoBean)object).setStartDate(splitData[2].split("=")[1]);
			break;
		case "12":
			object = new TodoBean();
			((TodoBean)object).setFileIdx(2);
			((TodoBean)object).setAccessCode(splitData[1].split("=")[1]);
			((TodoBean)object).setStartDate(splitData[2].split("=")[1]);
			((TodoBean)object).setEndDate(splitData[3].split("=")[1]);
			String visibleType = splitData[4].split("=")[1];
			if(!visibleType.equals("T")) {
				((TodoBean)object).setVisibleType(visibleType.equals("E") ? "O" : "X");
			}
		}
		
		return object;
	}
	
	private String convertServerData(ArrayList<TodoBean> toDoList) {
		StringBuffer serverData = new StringBuffer();
		
		for(TodoBean todo : toDoList) {
			serverData.append(todo.getStartDate().substring(6, 8));
			serverData.append(":");
		}
		
		if(serverData.length() != 0) serverData.deleteCharAt(serverData.length() - 1);
		
		return serverData.toString();
	}
	
	private String convertServerData2(ArrayList<TodoBean> toDoList) {
		StringBuffer serverData = new StringBuffer();
		
		for(TodoBean todo : toDoList) {
			serverData.append(todo.getAccessCode() != null ? todo.getAccessCode() + ",": "");
			serverData.append(todo.getStartDate() != null ? todo.getStartDate() + "," : "");
			serverData.append(todo.getEndDate() != null ? todo.getEndDate() + "," : "");
			serverData.append(todo.getContent() != null ? todo.getContent() + "," : "");
			serverData.append(todo.getStatus() != null ? todo.getStatus() + "," : "");
			serverData.append(todo.isIsactive() + ",");
			serverData.append(todo.getComment() != null ? todo.getComment() + "," : "");
			
			if(serverData.charAt(serverData.length() - 1) == ',') {
				serverData.deleteCharAt(serverData.length() - 1);
			}
			serverData.append(":");
		}
		if(serverData.charAt(serverData.length() - 1) == ':') {
			serverData.deleteCharAt(serverData.length() - 1);
		}
		
		return serverData.toString();
	}
	
}
