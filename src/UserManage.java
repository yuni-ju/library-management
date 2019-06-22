import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class UserManage extends User {
	static java.util.Scanner in = null;

	UserManage() {
		super();
	}
	
	void showList() {

		System.out.println("< 사용자 목록 >");
		System.out.println("===================================================");
		System.out.println("ID        이름           구분           전화번호                    예약 정보             ");
		System.out.println("===================================================");
		fileRead(true);
		System.out.println("");
	}

	void AdminRental() {
		
		while (true) {
			int menu_no;
			in = new java.util.Scanner(System.in);
			System.out.println("******************");
			System.out.println("이용자 리스트 보기 ==> 1");
			System.out.println("도서리스트 보기     ==> 2");
			System.out.println("대출하기               ==> 3");
			System.out.println("돌아가기               ==> 4");
			System.out.println("******************");
			System.out.print("input menu number :");
			menu_no = in.nextInt();
			
			Book book = new Book();
			
			switch (menu_no) {
			case 1:
				showList();
				break;
			case 2:
				book.showList(true);
				break;
			case 3:
				UserRental();
				break;
			case 4:
				return;
			default:
				System.out.println("메뉴 번호를 다시 입력하세요");
			}
		}
	}

	void AdminTurnIn() {
		while (true) {
			int menu_no;
			in = new java.util.Scanner(System.in);
			System.out.println("******************");
			System.out.println("이용자 리스트 보기 ==> 1");
			System.out.println("도서리스트 보기     ==> 2");
			System.out.println("반납하기               ==> 3");
			System.out.println("돌아가기               ==> 4");
			System.out.println("******************");
			System.out.print("input menu number :");
			menu_no = in.nextInt();
			
			Book book = new Book();
			
			switch (menu_no) {
			case 1:
				showList();
				break;
			case 2:
				book.showList(true);
				break;
			case 3:
				UserTurnIn();
				break;
			case 4:
				return;
			default:
				System.out.println("메뉴 번호를 다시 입력하세요");
			}
		}
		
	}

	void AdminReserve() {
		while (true) {
			int menu_no;
			in = new java.util.Scanner(System.in);
			System.out.println("******************");
			System.out.println("이용자 리스트 보기 ==> 1");
			System.out.println("도서리스트 보기     ==> 2");
			System.out.println("예약하기               ==> 3");
			System.out.println("돌아가기               ==> 4");
			System.out.println("******************");
			System.out.print("input menu number :");
			menu_no = in.nextInt();
			
			Book book = new Book();
			
			switch (menu_no) {
			case 1:
				showList();
				break;
			case 2:
				book.showList(true);
				break;
			case 3:
				UserReserve();
				break;
			case 4:
				return;
			default:
				System.out.println("메뉴 번호를 다시 입력하세요");
			}
		}
		
	}

	void AdminUserManage() {

		showList();

		in = new java.util.Scanner(System.in);
		int no;

		System.out.print("사용자 추가 => 1 , 사용자 삭제  => 2 , 돌아가기 => 3 : ");
		no = in.nextInt();

		if (no == 1) {
			addItem();
		} else if (no == 2) {
			eraseItem();
		} else if (no == 3) {
			;
		}

	}

	void addItem() {
		try {
			FileOutputStream out = new FileOutputStream(user_fn, true);
			boolean cont = true;
			in = new java.util.Scanner(System.in);

			int no = 0;

			int len = 0;
			// id=8, name=10, status=10, phone=11, reservebook=10(인덱스);
			while (cont) {
				System.out.println("ID : ");
				id = in.nextLine();
				
				len = id.length();
				for (int i = len; i < 8; i++) {
					id = id + " ";
				}				
				
				fileRead(false);
				if (success == true) {
					System.out.println("이미 존재하는 ID입니다.");
					return ;
				}

				System.out.println("이름 : ");
				name = in.nextLine();
				System.out.println("학부생 => 1 대학원생 =>2 교수 =>3 교직원 =>4 : "); // 입력시 한글 깨지는 문제..
				no = Integer.parseInt(in.nextLine());
				if (no == 1) {
					status = "학부생";
				} else if (no == 2) {
					status = "대학원생";
				} else if (no == 3) {
					status = "교수";
				} else if (no == 4) {
					status = "교직원";
				}
				System.out.println("전화번호 : ");
				phone = in.nextLine();

				for (int i = 0; i < recordSize; i++) {
					oneLine[i] = 0;
				}
				len = id.length();
				for (int i = len; i < 8; i++) {
					id = id + " ";
				}
				len = name.length();
				for (int i = len; i < 10; i++) {
					name = name + " ";
				}
				len = status.length();
				for (int i = len; i < 10; i++) {
					status = status + " ";
				}
				len = phone.length();
				for (int i = len; i < 11; i++) {
					phone = phone + " ";
				}
				for (int i = 0; i < 10; i++) { // 예약 칸 만들어주기
					phone = phone + " ";
				}

				id = id + name + status + phone;
				oneLine = id.getBytes();

				out.write(oneLine);
				System.out.print("Continue?(y/n) ");
				String ans;
				ans = in.nextLine();
				if (ans.compareTo("y") != 0)
					cont = false;
			}

			out.close();

		} catch (IOException e) {
			System.out.println("File Open Error!!!");
		}

	}

	void eraseItem() {
		in = new java.util.Scanner(System.in);
		String ans;
		String temp = "";
		System.out.print("삭제 할 ");
		Confirm();
		// 해당도서가 없습니다.
		if (success == false) {
			return;
		}
		// 대출중인지 확인
		for (int i = 0; i < 16; i++) {
			temp += " ";
		}
		String temp2 = "";
		for (int i = 0; i < 10; i++) {
			temp2 += " ";
		}
		

		//책파일읽어서 이 학번 가진 책 있는지 확인 하고 없을경우, 예약 정보 없을경우. 
		boolean delete = false;
		Book book = new Book();
		String conlent = "lent"+id;
		book.fileRead(conlent);
		if(book.lentcount == 0)
		{
			 delete = true;
		}
		
		if (delete == true && (reservebook).compareTo(temp2) == 0) {
			// 대출중 아니면
			System.out.println("정말 삭제하시겠습니까?(y/n) : ");
			ans = in.nextLine();
			if (ans.compareTo("y") == 0) {
				try {
					// book 파일 수정
					BufferedReader fin = null;
					fin = new BufferedReader(new FileReader(user_fn));

					String line = "";
					String allLine;
					String tempLine = "";
					byte[] allbyte;

					// index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

					while ((allLine = fin.readLine()) != null) {
						for (int i = 0; i < allLine.length(); i += recordSize) {
							line = allLine.substring(i, i + recordSize);
							if (id.compareTo(line.substring(0, 8)) != 0) {
								tempLine += line;
							}
						}

					}

					fin.close();

					FileOutputStream out = new FileOutputStream(user_fn);
					allbyte = tempLine.getBytes();
					out.write(allbyte);
					out.close();

				} catch (IOException e) {
					System.out.println("File Open Error!!!");
				}

			}
		} else {
			System.out.println("대출중이거나 예약중인 도서가 있습니다.");
		}
	}

}
