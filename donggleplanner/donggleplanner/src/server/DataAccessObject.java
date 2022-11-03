package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import server.beans.MemberBean;
import server.beans.TodoBean;
import server.beans.AccessHistoryBean;

// Data File과의 통신 용도
// 판단 불가! 통신만을 담당한다.
public class DataAccessObject {
	String[] fileInfo = {
			"C:\\java\\data\\donggleplanner\\src\\database\\MEMBERS.txt",
			"C:\\java\\data\\donggleplanner\\src\\database\\ACCESSHISTORY.txt",
			"C:\\java\\data\\donggleplanner\\src\\database\\TODO.txt"
			};
	
	public DataAccessObject() {
		
	}
	
	/* 회원 정보 수집 */
	public ArrayList<MemberBean> readDatabase(int fileIdx) {
		ArrayList<MemberBean> memberList = null;
		BufferedReader buffer = null;
		MemberBean member;
		
		try {
			buffer = new BufferedReader(new FileReader(new File(fileInfo[fileIdx])));
			memberList = new ArrayList<MemberBean>();
			
			while(true) {
				
				String line = buffer.readLine();
				if(line == null) break;
				String[] record = line.split(",");
				member = new MemberBean();
				
				member.setAccessCode(record[0]);
				member.setSecretCode(record[1]);
				member.setUserName(record[2]);
				member.setPhoneNumber(record[3]);
				member.setActivation(Integer.parseInt(record[4]));
				
				memberList.add(member);
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("파일이 존재하지 않습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			memberList = null;
			System.out.println("파일로부터 데이터를 가져올 수 없습니다.");
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return memberList;
	}
	
	/* 접속 기록 */
	public boolean writeAccessHistory(AccessHistoryBean accessInfo) {
		boolean result = false;
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(fileInfo[accessInfo.getFileIdx()]), true)); // 덮어쓰기가 아닌 추가를 위한 true
			writer.write(accessInfo.getAccessCode() + "," + accessInfo.getDate() + "," + accessInfo.getAccessType());
			writer.newLine();
			writer.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close(); // close를 호출하면 flush는 자동으로 호출된다. write는 flush 명령어에 의해서 실행된다!
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/* 할 일 목록 불러오기 */
	public ArrayList<TodoBean> gettoDoList(TodoBean searchInfo) {
		ArrayList<TodoBean> todoList = new ArrayList<TodoBean>();
		BufferedReader buffer = null;
		int date, recordCount = 1;
		int[] dateRange = new int[2];
		TodoBean todo = null;
		String line;
		
		LocalDate userDate = LocalDate.of(Integer.parseInt(searchInfo.getStartDate().substring(0, 4)),
				Integer.parseInt(searchInfo.getStartDate().substring(4)), 1);
		
		try {
			buffer = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIdx()])));
			
			while((line = buffer.readLine()) != null) {
				
				String[] record = line.split(",");
				if(!searchInfo.getAccessCode().equals(record[0])) continue;
				
				date = Integer.parseInt(searchInfo.getStartDate());
				dateRange[0] = Integer.parseInt(record[1].substring(0,8));
				dateRange[1] = Integer.parseInt(record[2].substring(0,8));
				
				if(date > dateRange[0] / 100) {
					dateRange[0] = Integer.parseInt(date + "01");
				}
				if(date < dateRange[1] / 100) {
					dateRange[1] = Integer.parseInt(date + "" + userDate.lengthOfMonth());
				}
				
				for(int idx = dateRange[0]; idx <= dateRange[1]; idx++) {
					
					if(recordCount != 1) {
						if(this.duplicateCheck(todoList, idx + "")) {
							continue;
						}
					}
					
					todo = new TodoBean();
					todo.setStartDate(idx+"");
					todoList.add(todo);
				}
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null) buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return todoList;
	}
	
	public ArrayList<TodoBean> getTodo(TodoBean searchInfo){
		ArrayList<TodoBean> toDoList = new ArrayList<TodoBean>();
		TodoBean todo = new TodoBean();
		BufferedReader reader = null;
		String line;
		String[] record;
		
		try {
			reader = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIdx()])));
			
			while((line = reader.readLine()) != null) {
				
				record = line.split(",");
				
				if(!searchInfo.getAccessCode().equals(record[0])) continue;
				if(searchInfo.getVisibleType() != null) {
					if(record[5].equals(searchInfo.getVisibleType())) continue;
				}
				
//				if(searchInfo.getVisibleType().equals("E")) {
//					if(!record[5].equals("O")) continue;
//				}
//				if(searchInfo.getVisibleType().equals("D")) {
//					if(!record[5].equals("X")) continue;
//				}

				for(int searchDate = Integer.parseInt(searchInfo.getStartDate().substring(0, 8));
						searchDate <= Integer.parseInt(searchInfo.getEndDate().substring(0, 8));
						searchDate++) {
					for(int toDoDate = Integer.parseInt(record[1].substring(0, 8));
							toDoDate <= Integer.parseInt(record[2].substring(0, 8));
							toDoDate++) {
						if(searchDate == toDoDate) {
							todo = new TodoBean();
							todo.setAccessCode(searchDate + "");
							todo.setStartDate(record[1]);
							todo.setEndDate(record[2]);
							todo.setContent(record[3]);
							todo.setStatus(record[4]);
							todo.setIsactive(record[5].equals("O") ? true : false);
							todo.setComment(record[6]);
							
							toDoList.add(todo);
						}
					}
				}
		
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return toDoList;
	}
	
	private boolean duplicateCheck(ArrayList<TodoBean> todoList, String compareValue) {
		boolean result = false;
		for(int listIdx=0; listIdx<todoList.size(); listIdx++) {
			if(compareValue.equals(todoList.get(listIdx).getStartDate())) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
}
