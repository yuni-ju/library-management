import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User {

	static java.util.Scanner in = null;
	String user_fn = "userinfo.bin";
	String id = " ";
	String name, status, phone , reservebook;
	String datestr;
	final int recordSize = 49;
	byte[] oneLine = new byte[recordSize];
	int returnday;
	int borrownum;
	// id=8, name=10, status=10, phone=11, reservebook=10(인덱스);
	boolean success = false;
	
	//사용자 정보
	void UserInfo() {
		Confirm();
		if (success == true) {
			System.out.println("ID : " + id);
			System.out.println("이름 : " + name);
			System.out.println("구분 : " + status);
			System.out.println("전화번호 : " + phone);			
			System.out.println("예약 정보 : " + reservebook);
			System.out.println("대출 정보: ");
			Book book = new Book();
			String lentbook = "lentbook" + id;
			System.out.println("================================================");
			System.out.println(
					"인덱스   제목                                             정상반납일                               ");
			System.out.println("================================================");
			book.fileRead(lentbook);
			System.out.println("");
		}
	}

	//사용자 확인
	void Confirm() {
		int len = 0;
		System.out.print("ID를 입력하세요 : ");
		in = new java.util.Scanner(System.in);
		id = in.nextLine();
		// 글자 크기 맞춰주기
		len = id.length();

		fileRead(false);

		if (success == false) {
			System.out.println("등록되지 않은 ID입니다.");
		}

	}

	//파일 읽기
	void fileRead(boolean print) {
		try {
			BufferedReader fin = null;
			int len = 0;
			fin = new BufferedReader(new FileReader(user_fn));
			String line = "";
			String allLine;
			// id=8, name=10, status=10, phone=11, reservebook=10(인덱스);
			while ((allLine = fin.readLine()) != null) {
				// 한줄 씩
				for (int i = 0; i < allLine.length(); i += recordSize) {
					// System.arraycopy(allLine, i, line, 0, recordSize);
					line = allLine.substring(i, i + recordSize);
					if (print == true) {
						System.out.println(line.substring(0, 8) + " " + line.substring(8, 18) +
								" " + line.substring(18, 28) + " " + line.substring(28, 39)
								+ "      " + line.substring(39, 49));
					} else if (id.compareTo(line.substring(0, 8)) == 0) {
						id = line.substring(0, 8);
						name = line.substring(8, 18);
						status = line.substring(18, 28);
						phone = line.substring(28, 39);					
						reservebook = line.substring(39, 49);
						success = true;
						break;
					}
				}
			}

			fin.close();

		} catch (FileNotFoundException e) {
			// TODO: handle exception
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	void UserClassify() {

		Member mem = null; 
		
		String s = status.trim();
		
		switch(s)
		{
		case "학부생":
			mem= new Undergraduate();	
			break;
		case "대학원생":
			mem= new Postgraduate();
			break;
		case "교수":
			mem = new Professor();
			break;
		case "교직원":
			mem = new Staff();
			break;
		}
		returnday = mem.Retrundays();
		borrownum = mem.Borrownum();
		
		//System.out.println("반납일:"+mem.Retrundays()+"일 후");
		//System.out.println("대여가능 도서 수:"+mem.Borrownum());

	}

	void UserRental() {
		
		String temp = "";

		//이용자 확인
		Confirm();

		if (success == true) {
			
			//이용자 분류
			UserClassify();
			
			//몇권 빌릴수있는지 확인하고 못빌리면 안된다고 하기. 
			Book book = new Book();
			
			String conlent = "lent"+id;
			book.fileRead(conlent);
			
			if(book.lentcount>=borrownum)
			{
				System.out.println("대출이 불가능합니다.");
				System.out.println("현재 대여한 책 권 수("+book.lentcount+"), 최대 대여 가능 권수("+borrownum+")");
				return ;
			}
			
			//책 대여			
			book.Confirm(true);
			if (book.success == false) {
				System.out.println("등록되지 않은 책입니다.");
				return ;
			}
			
			for (int i = 0; i < 10; i++) {
				temp += " ";
			}
			
			//책의 예약정보가 차있고 유저의 예약정보 인덱스랑 다르면 대출 불가능. 
			if ((book.reserveinfo.compareTo(temp) != 0 && book.index.trim().compareTo(reservebook.trim()) != 0)) {
				System.out.println("예약중인 도서입니다.");
				return ;
			}
			temp="";
			for (int i = 0; i < 16; i++) {
				temp += " ";
			}
			if ((book.borrowinfo).compareTo(temp) == 0) {
				System.out.println("대출하시겠습니까?(y/n) : ");
				String ans;
				ans = in.nextLine();
				if (ans.compareTo("y") == 0) {
					try {
						int len = 0;
						//반납날짜 넣기
						Date date = new Date();
						SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);

						cal.add(Calendar.DATE,returnday);
						datestr = dataFormat.format(cal.getTime());
							book.borrowinfo = id + datestr;
	
							len = book.borrowinfo.length();
							for (int i = len; i < 16; i++) {
								book.borrowinfo = book.borrowinfo + " ";
							}

							//예약한사람이 대출한다면 예약정보 없애기 
							if(book.index.trim().compareTo(reservebook.trim()) == 0){
								
								temp="";						
								for (int i = 0; i < 10; i++) {
									temp += " ";
								}
								
								//책예약정보 초기화
								book.reserveinfo=temp;
								
								//유저 예약정보 초기화
								reservebook = temp;
								
								//유저 파일 수정 
								String s = id + name + status + phone + reservebook;
								
								BufferedReader fin = null;
								fin = new BufferedReader(new FileReader(user_fn));
								
								//변수들 초기화
								String line = "";
								String allLine = "";
								String tempLine = "";
								byte[] allbyte;
								
								// id=8, name=10, status=10, phone=11, reservebook=10(인덱스);
								while ((allLine = fin.readLine()) != null) {
									for (int i = 0; i < allLine.length(); i += recordSize) {
										line = allLine.substring(i, i + recordSize);
										if (id.compareTo(line.substring(0, 8)) == 0) {
											tempLine += s;
										} else {
											tempLine += line;
										}
									}

								}
								fin.close();
								FileOutputStream out = null;
								out = new FileOutputStream(user_fn);
								allbyte = tempLine.getBytes();
								out.write(allbyte);
								out.close();					
								
								
							}
							
						String s = book.index + book.name + book.borrowinfo + book.reserveinfo;
						//System.out.println(s);

						
						
						// book 파일 수정
						BufferedReader fin = null;
						fin = new BufferedReader(new FileReader(book.book_fn));

						String line = "";
						String allLine;
						String tempLine = "";
						byte[] allbyte=null;

						// index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

						while ((allLine = fin.readLine()) != null) {
							for (int i = 0; i < allLine.length(); i += book.recordSize) {
								line = allLine.substring(i, i + book.recordSize);
								if (book.index.compareTo(line.substring(0, 5)) == 0) {
									tempLine += s;
								} else {
									tempLine += line;
								}
							}

						}
						fin.close();

						FileOutputStream out = new FileOutputStream(book.book_fn);
						allbyte = tempLine.getBytes();
						out.write(allbyte);
						out.close();

					} catch (IOException e) {
						System.out.println("File Open Error!!!");
					}

					System.out.println("대출 완료! 반납은 " + datestr + "까지 입니다.");
				} else {
					System.out.println("취소되었습니다.");
				}
			} else {
				System.out.println("이미 대출 중인 도서입니다.");
			}
		}
	}
	
	void UserTurnIn() {
		int len;
		in = new java.util.Scanner(System.in);
		boolean cont = true;
		boolean lentsuccess=false;
		// 이용자 확인
		Confirm();

		if (success == true) {
			// 빌린 책 목록 띄우기
			Book book = new Book();
			String lentbook = "lentbook" + id;
			System.out.println("================================================");
			System.out.println(
					"인덱스   제목                                             정상반납일                               ");
			System.out.println("================================================");
			book.fileRead(lentbook);
			
			while (cont) {
				// 반납할 책 인덱스 입력				
				System.out.print("반납하실 도서 index를 입력하세요 : ");
				in = new java.util.Scanner(System.in);
				book.index = in.nextLine();
				len = book.index.length();
				for (int i = len; i < 5; i++) {
					book.index = book.index + " ";
				}
				
				for(int i=0;i<book.lentstr.length();i+=5) {
					//System.out.println(book.lentstr.substring(i, i + 5));
					if(book.index.compareTo(book.lentstr.substring(i, i + 5))==0)
						lentsuccess=true;					
				}
				book.fileRead("conindex");
				
				// book 파일에서 그 책만 수정
				if (lentsuccess == true) {
					
					try {					
						String temp="";						
							for (int i = 0; i < 16; i++) {
								temp += " ";
							}
							
						book.borrowinfo=temp;

						String s = book.index + book.name + book.borrowinfo + book.reserveinfo;
						//System.out.println(s);

						// book 파일 수정
						BufferedReader fin = null;
						fin = new BufferedReader(new FileReader(book.book_fn));

						String line = "";
						String allLine;
						String tempLine = "";
						byte[] allbyte;

						// index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

						while ((allLine = fin.readLine()) != null) {
							for (int i = 0; i < allLine.length(); i += book.recordSize) {
								line = allLine.substring(i, i + book.recordSize);
								if (book.index.compareTo(line.substring(0, 5)) == 0) {
									tempLine += s;
								} else {
									tempLine += line;
								}
							}

						}
						fin.close();

						FileOutputStream out = new FileOutputStream(book.book_fn);
						allbyte = tempLine.getBytes();
						out.write(allbyte);
						out.close();

					} catch (IOException e) {
						System.out.println("File Open Error!!!");
					}
					
					System.out.println(book.name+" 반납 완료");
					book.lentstr="";
			
					
				} else {
					System.out.println("해당 책이 없습니다.");
				}
				System.out.print("Continue?(y/n) ");
				String ans;
				ans = in.nextLine();
				if (ans.compareTo("y") != 0)
					cont = false;

			}
		}

	}

	void UserReserve() {

		//이용자 확인
		Confirm();
		
		//이미 예약중인지 확인 
		String temp ="";
		for (int i = 0; i < 10; i++) {
			temp += " ";
		}
	
		if ((reservebook).compareTo(temp) == 0) {
		
		//예약할 책 인덱스 입력		
		Book book = new Book();
		System.out.print("예약하실 ");
		book.Confirm(true);

		if (book.success == false) {
			System.out.println("등록되지 않은 책입니다.");
			return ;
		}
		
		temp ="";
		//이미 예약된 책입니다. 
		for (int i = 0; i < 10; i++) {
			temp += " ";
		}
		
		if (book.reserveinfo.compareTo(temp) != 0) {
			System.out.println("이미 예약된 책입니다.");
			return ;
		}
		
		//예약정보 y로 수정
		
			System.out.println("예약하시겠습니까?(y/n) : ");
			String ans;
			ans = in.nextLine();
			if (ans.compareTo("y") == 0) {
				try {
					int len = 0;
						
						book.reserveinfo = "y";					
						len = book.reserveinfo.length();
						
						for (int i = len; i < 10; i++) { //예약 크기 맞춰주기
							book.reserveinfo = book.reserveinfo + " ";
						}
				

					String s = book.index + book.name + book.borrowinfo + book.reserveinfo;
					//System.out.println(s);

					// book 파일 수정
					BufferedReader fin = null;
					fin = new BufferedReader(new FileReader(book.book_fn));

					String line = "";
					String allLine;
					String tempLine = "";
					byte[] allbyte;

					// index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

					while ((allLine = fin.readLine()) != null) {
						for (int i = 0; i < allLine.length(); i += book.recordSize) {
							line = allLine.substring(i, i + book.recordSize);
							if (book.index.compareTo(line.substring(0, 5)) == 0) {
								tempLine += s;
							} else {
								tempLine += line;
							}
						}

					}
					fin.close();

					FileOutputStream out = new FileOutputStream(book.book_fn);
					allbyte = tempLine.getBytes();
					out.write(allbyte);
					out.close();
	
					//내 정보에는 예약부분에 책인덱스입력
				
					len = 0;			
					
					reservebook = book.index;
					len = reservebook.length();
					
					for (int i = len; i < 10; i++) { 
						reservebook = reservebook + " ";
					}
				
					s = id + name + status + phone + reservebook;
					fin = new BufferedReader(new FileReader(user_fn));
					
					//변수들 초기화
					line = "";
					allLine = "";
					tempLine = "";
					allbyte = null;
					
					// id=8, name=10, status=10, phone=11, reservebook=10(인덱스);
					while ((allLine = fin.readLine()) != null) {
						for (int i = 0; i < allLine.length(); i += recordSize) {
							line = allLine.substring(i, i + recordSize);
							if (id.compareTo(line.substring(0, 8)) == 0) {
								tempLine += s;
							} else {
								tempLine += line;
							}
						}

					}
					fin.close();

					out = new FileOutputStream(user_fn);
					allbyte = tempLine.getBytes();
					out.write(allbyte);
					out.close();					
					
					System.out.println("예약완료"); 				
					
				} catch (IOException e) {
					System.out.println("File Open Error!!!");
				}
				
			} else {
				System.out.println("취소되었습니다.");
			}
		}
		else {
			System.out.println("이미 예약중인 도서가 있습니다.");
		}

	}

}
