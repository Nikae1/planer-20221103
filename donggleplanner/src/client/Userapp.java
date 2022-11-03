package client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import server.ServerController;

public class Userapp {

	public Userapp() {

		frontController();

	}

	private void frontController() {
		Scanner scanner = new Scanner(System.in);
		ServerController ctl = new ServerController();
		TaskManagement task = null;

		boolean isLoop = true;
		boolean accessResult;

		String[] accessInfo = new String[2];
		String[] itemName = { "id", "pw" };

		String mainTitle = this.mainTitle(this.getToday(true));
		String mainMenu = this.makeMenu();
		String close = this.close();
		String message = null;

		while (isLoop) {

			for (int idx = 0; idx < accessInfo.length; idx++) {
				this.display(mainTitle);
				this.display(this.makeAccesse(true, accessInfo[0]));
				accessInfo[idx] = this.userInput(scanner);
			}
			this.display(this.makeAccesse(false, null));
			// 로그인 정보 전달
			accessResult = ctl.controller(this.makeClientData("1", itemName, accessInfo)).equals("1") ? true : false;

			this.display(this.accessResult(accessResult));

			if (!accessResult) {
				if (this.userInput(scanner).toUpperCase().equals("N")) {
					isLoop = false;
				} else {
					accessInfo[0] = null;
					accessInfo[1] = null;
				}
			} else {
				accessInfo[1] = null;

				while (isLoop) {

					String menuSelection = new String();
					
					while (true) { // 로그인 성공

						this.display(mainTitle);
						this.display(message != null ? "\n[Message] " + message + "\n\n": "");
						this.display(mainMenu);
						
						menuSelection = this.userInput(scanner);

						if (this.isInteger(menuSelection)) {
							if (this.isIntegerRange(Integer.parseInt(menuSelection), 0, 4)) {
								break;
							} else {
								message = "범위 내로 입력해 주세요.";
								break;
							}
						} else {
							message = "숫자로 입력해 주세요.";
							break;
						}
					}

					if (menuSelection.toUpperCase().equals("0")) {
						ctl.controller(this.makeClientData("-1", itemName, accessInfo));
						isLoop = false;
					} else {
						task = new TaskManagement();

						if (menuSelection.equals("1")) {

							String[] userInput = new String[3];
	
							String[] userSelection = { "Start Date | ", "End Date | ", "Total  Enable  Disable | " };
							int month = 0;

							while (true) {
								menuSelection = "11";
								this.display(mainTitle);
								this.display(task.taskController(Integer.parseInt(menuSelection), accessInfo[0], month).toString());

								// Start Date
								char direction = 'p';

								for (int idx = 0; idx < userInput.length; idx++) {

									this.display(userSelection[idx]);
									userInput[idx] = this.userInput(scanner).toUpperCase();

									if (userInput[idx].equals("P") || userInput[idx].equals("N")) {
										month += userInput[idx].equals("P") ? -1 : 1;
										break;
									} else if (userInput[idx].equals("Q")) {
										direction = 'b';
										break;
									} else if (idx == userInput.length - 1) {
										if (userInput[idx].equals("T") || userInput[idx].equals("E") || userInput[idx].equals("D")) {
											menuSelection = "12";
											String clienData = this.makeClientData(accessInfo, userInput, menuSelection);
											this.display(task.taskController(clienData).toString());
										}
									}

								}

								if (direction == 'b')
									break;
								if (direction == 'c')
									continue;
							}

						}
					}
				}
			}

		}
		this.display(close);
		scanner.close();
	}// 화면 컨트롤러

	private String mainTitle(String date) {
		StringBuffer title = new StringBuffer();

		title.append("\n");
		title.append("◤Donggle◥〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		title.append("∥\n");
		title.append("∥     D◎nggle D◎nggle      ╭◜◝ ͡ ◜◝        ╭◜◝ ͡ ◜◝                \n");
		title.append("∥	    ✿ Planner ✿   ( •‿•。   ) ☆   ( •‿•。   ) ☆                   \n");
		title.append("∥	                   ╰ ◟◞ ͜ ◟◞╭◜◝ ͡ ◜◝╮  ͜    ◟◞╯       \n");
		title.append("∥	                         ( •‿•。     ) ☆       \n");
		title.append("∥	 ╭◜◝ ͡ ◜◝╮                ╰  ◟◞ ͜ ◟◞  ╯      \n");
		title.append("∥       ( ＊•◡•＊ )                                 \n");
		title.append("∥        ╰ ◟◞ ͜ ◟ ◞╯               " + date + "\n");
		title.append("∥                           ✿°。Designed by. 4조 ✿° \n");
		title.append("∥                        박초롱 윤지수 주성현 김지웅\n");
		title.append("∥\n");
		title.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");

		return title.toString();
	}// 메인 타이틀

	private String makeAccesse(boolean isItem, String AccessCode) {

		StringBuffer accesse = new StringBuffer();

		if (isItem) {
			accesse.append("◤ACCESSE◥〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
			accesse.append("\n");
			accesse.append("	      ____________          ____________\n");
			accesse.append("	     ｜AccessCode｜        ｜SecretCode｜    \n");
			accesse.append("	      ￣￣￣￣￣￣          ￣￣￣￣￣￣     \n");
			accesse.append("                 " + ((AccessCode != null) ? AccessCode + "                " : ""));
		} else {
			accesse.append("\n");
			accesse.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n\n");
		}

		return accesse.toString();

	}// 액세스

	private String accessResult(boolean isAccess) {
		StringBuffer accessResult = new StringBuffer();

		for (int i = 0; i < 6; i++) {
			this.display("❥ ");

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.display("Loading...\n");

		if (isAccess) {
			accessResult.append("\n");
			accessResult.append("❥ ❥ ❥ ❥ ❥ ❥ ❥ ❥ Successful Connection !\n");
			accessResult.append("                     Move after 2 sceonds...");
			accessResult.append("\n");
		} else {
			accessResult.append("\n");
			accessResult.append("❥ ❥ ❥ ❥ ❥ ❥ ❥ ❥ Connection Failed\n");
			accessResult.append("_________________________________________ Retry(Y/N) ? : ");
		}

		return accessResult.toString();
	}

	private String makeMenu() {
		StringBuffer menu = new StringBuffer();

		menu.append("◤MENU◥ 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		menu.append("∥\n");
		menu.append("∥	     1. TASK LIST     	   2. TASK SETTINGS\n");
		menu.append("∥	     3. MODIFY TASK        4. TASK STATS\n");
		menu.append("∥	     0. DISCONNECT\n");
		menu.append("∥\n");
		menu.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓 Select: ");

		return menu.toString();
	}// 메뉴

	private String close() {
		StringBuffer close = new StringBuffer();

		close.append("◤G◎◎D BYE !◥ 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		close.append("∥\n");
		close.append("∥	        See you ~ ! ( ˘▽˘)っ♡ \n");
		close.append("∥\n");
		close.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		return close.toString();
	}

	/* 클라이언트 데이터 나누기 */
	private String makeClientData(String serviceCode, String[] item, String[] userData) {
		StringBuffer clientData = new StringBuffer();

		clientData.append("serviceCode=" + serviceCode);
		for (int idx = 0; idx < userData.length; idx++) {
			clientData.append("&");
			clientData.append(item[idx] + "=" + userData[idx]);
		}

		return clientData.toString();
	}
	
	private String makeClientData(String[] accessInfo, String[] userInput, String menuSelection) {
		
		StringBuffer clientData = new StringBuffer();
				clientData.append("serviceCode=" + menuSelection +
						"&accessCode=" + accessInfo[0] + "&startDate=" + userInput[0] +
						"&endDate=" + userInput[1] + "&visibleType=" + userInput[2]);
				
		return clientData.toString();
	}

	private String getToday(boolean isDate) {
		String pattern = (isDate) ? "yyyy. MM. dd." : "yyyy-MM-dd HH:mm:ss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/* 정수 변환여부 체크 */
	private boolean isInteger(String value) {
		boolean isResult = true;
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			isResult = false;// e.printStackTrace();
		}
		return isResult;
	}

	/* 문자 > 정수 변환 */
	private int convertToInteger(String value) {
		return Integer.parseInt(value);
	}

	/* 정수의 범위 체크 */
	private boolean isIntegerRange(int value, int starting, int last) {
		return (value >= starting && value <= last) ? true : false;
	}

	/* TaskList를 계속 진행 */
	private boolean isBreak(String keyValue) {
		return keyValue.toUpperCase().equals("Q") ? true : false;
	}

	/* TaskList를 다시 실행 */
	private boolean isRestart(String keyValue) {
		return keyValue.toUpperCase().equals("P") || keyValue.toUpperCase().equals("N") ? true : false;
	}

	private String userInput(Scanner scanner) {
		return scanner.next();
	}// 사용자 입력

	private void display(String text) {
		System.out.print(text);
	}// 화면 출력

}// Userapp
