package client;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import server.ServerController;

public class TaskManagement {
	
	private ServerController server;
	
	public TaskManagement() {
		
		server = new ServerController();
		
	}

	public Object taskController(int selection, String accessCode, int month) {
		Object result = null;
		
		switch(selection) {
		case 11:
			result = this.makeTaskCalendarCtl(accessCode, month);
			break;
		}
		
		return result;
	}
	
	//오버 로딩을 위해서는 파라미터의 갯수나 데이터 타입이 달라야 함.
	
	public Object taskController(String clientData) {
		Object result = null;
		
		switch(clientData.split("&")[0].split("=")[1]) {
		case "12":
			result = this.getTaskListCtl(clientData);
			break;
		}
		
		return result;
	}

	/* 특정한 달의 Task Calendar 생성하기 */
	private Object makeTaskCalendarCtl(String accessCode, int month) {
		LocalDate today = LocalDate.now().plusMonths(month);
		
		int[] taskDays = this.getTaskDays(server.controller("serviceCode=9&accessCode=" + accessCode +
				"&data=" + today.format(DateTimeFormatter.ofPattern("yyyyMM"))));
		
		return this.makeCalendar(taskDays, today);
	}
	
	
	/* 등록된 모든 할 일 리스트 가져오기 */
	private Object getTaskListCtl(String clientData) {
		return this.makeTodoList(this.server.controller(clientData).split(":"));
	}
	
	/* 정렬 및 할 일 리스트 출력 */
	public String makeTodoList(String[] record) {
		StringBuffer buffer = new StringBuffer();
		String temp;
		
		for(int idx = 0; idx < record.length; idx++) {
			if(idx != record.length -1) {
				for(int subIdx = idx + 1; subIdx < record.length; subIdx++) {
					if(Integer.parseInt(record[idx].substring(9, 17)) > Integer.parseInt(record[subIdx].substring(9, 17))) {
						temp = record[idx];
						record[idx] = record[subIdx];
						record[subIdx] = temp;
					}
				}
			}
			
		}
//		System.out.println(Arrays.toString(record));
		String[][] toDoList = new String[record.length][];
		
		for(int idx = 0; idx < record.length; idx++) {
			toDoList[idx] = record[idx].split(",");
		}
		
		String now;
		String tmp;
		int itemCount;
		int beginIdx = 0;
		
		while(true) {

			now = toDoList[beginIdx][1].substring(0, 8);
			itemCount = this.itemCount(toDoList, now);
			
			buffer.append("________________________________________________________\n\n");
			buffer.append(" To Do List (" + now.substring(0, 4) + ". " + now.substring(4, 6) + ". " + now.substring(6) + " / " + itemCount + ")\n\n");
			buffer.append("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		
			for(int itemIdx = beginIdx; itemIdx < (itemCount + beginIdx); itemIdx++) {
				
				buffer.append("\n   Status              Content              Enable\n");
				buffer.append("\n________________________________________________________\n\n");
				if(toDoList[itemIdx][4].equals("0")) {
					buffer.append("     □");
				}else if(toDoList[itemIdx][4].equals("1")) {
					buffer.append("      !");
				}else if(toDoList[itemIdx][4].equals("2")) {
					buffer.append("     ■");
				}
				buffer.append("           " + toDoList[itemIdx][3]);
				
				tmp = toDoList[itemIdx][5].equals("false") ? "                  X" : "                  O";
				
				if(toDoList[itemIdx][5] != null) buffer.append(tmp);
				if(toDoList[itemIdx][6] != null) buffer.append("\n                  └─ " + toDoList[itemIdx][6]);
				buffer.append("\n________________________________________________________\n");
				
			}
			
			if((itemCount + beginIdx) == toDoList.length) break;
			beginIdx += itemCount;
			
		}
		
		return buffer.toString();
	}
	
	private char[] typeof(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private int itemCount(String[][] list, String compareValue) {
		int itemCount = 0;
		for(int idx = 0; idx < list.length; idx++) {
			if(!compareValue.equals(list[idx][1].substring(0, 8))) break;
				itemCount += 1;
		}
		
		return itemCount;
	}
	
	/* 할 일 등록 하기 */
	private Object TaskRegistrationCtl(String accessCode) {
		return null;
	}
	
	/* 등록된 할 일 수정하기 */
	private Object setModifyTaskCtl(String accessCode) {
		return null;
	}
	
	/* 특정한 달의 할 일이 등록되어 있는 날짜 가져오기 */
	private int[] getTaskDays(String serverData) {
		int[] taskDays = null;
		
		if(!serverData.equals("")) {
			
			String[] splitData = serverData.split(":");
			taskDays = new int[splitData.length];
			
			for(int idx = 0; idx < taskDays.length; idx++) {
				taskDays[idx] = Integer.parseInt(splitData[idx]);
			}
			
		}
		
		
		
		return taskDays;
	}
	
	/* 할 일이 등록되어 있는 날짜를 특정 달의 달력에 표시하기 */
	private String makeCalendar(int[] days, LocalDate today) {
		StringBuffer calendar = new StringBuffer();
		int dayOfWeek = LocalDate.of(today.getYear(), today.getMonthValue(), 1).getDayOfWeek().getValue();
		int lastDay = today.lengthOfMonth();
		boolean isTask = false;
		
		dayOfWeek = (dayOfWeek == 7) ? 1 : dayOfWeek + 1;

		calendar.append("◤TASK LIST◥〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		calendar.append("\n");
		calendar.append("            Prev   [ " + today.format(DateTimeFormatter.ofPattern("yyyy. MM.")) + " ]   Next \n");
		calendar.append("          ________________________________\n");
		calendar.append("\n");
		calendar.append("   Sun    Mon    Tue    Wed    Thu    Fri    Sat   \n");
		
		for(int dayIdx = 1 - (dayOfWeek - 1); dayIdx <= lastDay; dayIdx++) {
			
			if(dayIdx < 1) {
				calendar.append("       ");
			}else {
				calendar.append(dayIdx < 10 ? "    " + dayIdx : "   "+ dayIdx);
				if(days != null) {
					for(int taskDayIdx = 0; taskDayIdx < days.length; taskDayIdx++) {
						if(dayIdx == days[taskDayIdx]) {
							isTask = true;
							break;
						}
					}
				}
				
				calendar.append(isTask ? "+ " : "  "); 
				isTask = false;
			}
			calendar.append((dayIdx + (dayOfWeek - 1)) % 7 == 0? "\n" : "");
		}
		
		calendar.append("\n");
		calendar.append("\n●〓〓〓〓〓〓〓〓〓〓〓〓 [P] Prev [N] Next [Q] 처음으로");
		calendar.append("\n\n");
		
		return calendar.toString();
	}
	
}
