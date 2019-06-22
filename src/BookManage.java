import java.io.*;

public class BookManage extends Book {
	static java.util.Scanner in = null;

	BookManage(){
		super();
	}
	
	void AdminBookManage() {

		showList(true);

		in = new java.util.Scanner(System.in);
		int no;

		System.out.print("도서 추가 => 1 , 도서 삭제  => 2 , 돌아가기 => 3 : ");
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
			FileOutputStream out = new FileOutputStream(book_fn, true);
			boolean cont = true;
			in = new java.util.Scanner(System.in);			
			int len = 0;
			byte[] oneLine = new byte[recordSize];
			// index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10
			while (cont) {
				System.out.println("도서번호 : ");
				index = in.nextLine();
				/////////인덱스 겹치는지 확인하고 저장해야됨	
				len = index.length();
				for (int i = len; i < 5; i++) {
					index = index + " ";
				}				
				fileRead("conindex");
				//해당도서가 없습니다. 
				if(success==true) {
					System.out.println("이미 존재하는 도서번호 입니다.");
					return;
				}
				
				System.out.println("도서제목 : ");
				name = in.nextLine();
				for (int i = 0; i < recordSize; i++) {
					oneLine[i] = 0;
				}
				len = index.length();
				for (int i = len; i < 5; i++) {
					index = index + " ";
				}
				len = name.length();
				for (int i = len; i < 20; i++) {
					name = name + " ";
				}
				for (int i = 0; i < 26; i++) { // 대출,예약 칸 만들어주기
					name = name + " ";
				}

				index = index + name;
				oneLine = index.getBytes();

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
		String temp="";
		String temp2="";
		System.out.print("삭제 할 ");
		Confirm(false);
		//해당도서가 없습니다. 
		if(success==false) {
			System.out.println("해당 도서가 없습니다.");
			return;
		}
		//대출중인지 확인 		
		for (int i=0; i<16; i++) { 
			temp+=" ";
		}
		for (int i=0; i<10; i++) { 
			temp2+=" ";
		}
		if((borrowinfo).compareTo(temp)==0&&(reserveinfo).compareTo(temp2)==0) {
			//대출중 아니면
		System.out.println("정말 삭제하시겠습니까?(y/n) : ");
		ans = in.nextLine();
		if (ans.compareTo("y") == 0) {
			try {
				// book 파일 수정
				BufferedReader fin = null;
				fin = new BufferedReader(new FileReader(book_fn));

				String line = "";
				String allLine;
				String tempLine = "";
				byte[] allbyte;

				// index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

				while ((allLine = fin.readLine()) != null) {
					for (int i = 0; i < allLine.length(); i +=recordSize) {
						line = allLine.substring(i, i + recordSize);
						if (index.compareTo(line.substring(0, 5)) != 0) {
							tempLine += line;
						}
					}

				}
				fin.close();

				FileOutputStream out = new FileOutputStream(book_fn);
				allbyte = tempLine.getBytes();
				out.write(allbyte);
				out.close();

			} catch (IOException e) {
				System.out.println("File Open Error!!!");
			}
			
		}
		}
		else {
			System.out.println("대출 중이거나 예약 중인 도서입니다.");
		}
	}

}
