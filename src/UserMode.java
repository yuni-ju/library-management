import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class UserMode {

    static Scanner input = null;
    Member member = new Member();
    Book book = new Book();
    BookDataManage bookManage = new BookDataManage();
    MemberDataManage memberManage = new MemberDataManage();
    String index = "";

    //대출
    void UserRental() {

        String temp = "";

        //이용자 확인
        member.setId(memberManage.Confirm());

        if (memberManage.success) {

            //이용자 분류
            memberManage.UserClassify();

            //몇권 빌릴 수 있는지 확인

            String conlent = "lent"+ member.getId();
            //System.out.println(conlent);

            bookManage.fileRead(conlent);


            if(book.getLentCount()>=memberManage.borrowNum)
            {
                System.out.println("대출이 불가능합니다.");
                System.out.println("현재 대여한 책 권 수("+book.getLentCount()+"), 최대 대여 가능 권수("+memberManage.borrowNum+")");
                return ;
            }

            //책 대여
            bookManage.Confirm(true);
            if (!bookManage.success) {
                System.out.println("등록되지 않은 책입니다.");
                return ;
            }

            for (int i = 0; i < 10; i++) {
                temp += " ";
            }



            //책의 예약정보가 차있고 유저의 예약정보 인덱스랑 다르면 대출 불가능.
            //System.out.println(bookManage.getReserveInfo() + " " + temp + " "+ bookManage.index.trim() +" "+ memberManage.getReserveBook());

            if ((bookManage.getReserveInfo().compareTo(temp) != 0 && bookManage.index.trim().compareTo(memberManage.getReserveBook().trim()) != 0)) {
                System.out.println("예약중인 도서입니다.");
                return ;
            }
            temp="";
            for (int i = 0; i < 16; i++) {
                temp += " ";
            }
            if ((bookManage.getBorrowInfo()).compareTo(temp) == 0) {
                System.out.println("대출하시겠습니까?(y/n) : ");
                input = new Scanner(System.in);
                String ans = input.next();
                if (ans.compareTo("y") == 0) {
                    try {
                        int len = 0;
                        //반납날짜 넣기
                        Date date = new Date();
                        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);

                        cal.add(Calendar.DATE,memberManage.returnDay);
                        memberManage.dateStr = dataFormat.format(cal.getTime());
                        bookManage.setBorrowInfo(memberManage.getId() + memberManage.dateStr);

                        len = bookManage.getBorrowInfo().length();
                        for (int i = len; i < 16; i++) {
                            bookManage.setBorrowInfo(bookManage.getBorrowInfo() + " ");
                        }

                        //예약한사람이 대출한다면 예약정보 없애기
                        if(bookManage.index.trim().compareTo(memberManage.getReserveBook().trim()) == 0){

                            temp="";
                            for (int i = 0; i < 10; i++) {
                                temp += " ";
                            }

                            //책예약정보 초기화
                            bookManage.setReserveInfo(temp);

                            //유저 예약정보 초기화
                            memberManage.setReserveBook(temp);

                            //유저 파일 수정
                            String s = memberManage.getId() + memberManage.getName() + memberManage.getStatus() + memberManage.getPhone() + memberManage.getReserveBook();

                            BufferedReader fin = null;
                            fin = new BufferedReader(new FileReader(memberManage.user_fn));

                            //변수들 초기화
                            String line = "";
                            String allLine = "";
                            String tempLine = "";

                            // id=8, name=10, status=10, phone=11, reservebook=10(인덱스);
                            while ((allLine = fin.readLine()) != null) {
                                for (int i = 0; i < allLine.length(); i += memberManage.recordSize) {
                                    line = allLine.substring(i, i + memberManage.recordSize);
                                    if (memberManage.getId().compareTo(line.substring(0, 8)) == 0) {
                                        tempLine += s;
                                    } else {
                                        tempLine += line;
                                    }
                                }

                            }
                            fin.close();
                            FileOutputStream fileout = null;
                            fileout = new FileOutputStream(memberManage.user_fn);
                            byte[] allbyte = tempLine.getBytes();
                            fileout.write(allbyte);
                            fileout.close();

                        }

                        String s = bookManage.index + bookManage.getName() + bookManage.getBorrowInfo() + bookManage.getReserveInfo();
                        //System.out.println(s);

                        // book 파일 수정
                        BufferedReader fin = null;
                        fin = new BufferedReader(new FileReader(bookManage.book_fn));

                        String line = "";
                        String allLine;
                        String tempLine = "";
                        byte[] allbyte=null;

                        // index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

                        while ((allLine = fin.readLine()) != null) {
                            for (int i = 0; i < allLine.length(); i += bookManage.recordSize) {
                                line = allLine.substring(i, i + bookManage.recordSize);
                                if (bookManage.index.compareTo(line.substring(0, 5)) == 0) {
                                    tempLine += s;
                                } else {
                                    tempLine += line;
                                }
                            }

                        }
                        fin.close();

                        FileOutputStream out = new FileOutputStream(bookManage.book_fn);
                        allbyte = tempLine.getBytes();
                        out.write(allbyte);
                        out.close();

                    } catch (IOException e) {
                        System.out.println("File Open Error!!!");
                    }

                    System.out.println("대출 완료! 반납은 " + memberManage.dateStr + "까지 입니다.");
                } else {
                    System.out.println("취소되었습니다.");
                }
            } else {
                System.out.println("이미 대출 중인 도서입니다.");
            }
        }
    }

    //반납
    void UserTurnIn() {
        int len;
        input = new Scanner(System.in);
        boolean cont = true;
        boolean lentsuccess=false;
        // 이용자 확인
        memberManage.Confirm();

        if (memberManage.success == true) {
            // 빌린 책 목록 띄우기
            Book book = new Book();
            String lentbook = "lentbook" + memberManage.getId();
            System.out.println("====================================================");
            System.out.println( "인덱스              제목             정상반납일       ");
            System.out.println("====================================================");
            bookManage.fileRead(lentbook);

            while (cont) {
                // 반납할 책 인덱스 입력
                System.out.print("반납하실 도서 index를 입력하세요 : ");
                input = new Scanner(System.in);
                bookManage.index = input.nextLine();
                len = bookManage.index.length();
                for (int i = len; i < 5; i++) {
                    bookManage.index = bookManage.index + " ";
                }

                for(int i=0;i<bookManage.lentStr.length();i+=5) {
                    //System.out.println(book.lentstr.substring(i, i + 5));
                    if(bookManage.index.compareTo(bookManage.lentStr.substring(i, i + 5))==0)
                        lentsuccess=true;
                }
                bookManage.fileRead("conindex");

                // book 파일에서 그 책만 수정
                if (lentsuccess == true) {

                    try {
                        String temp="";
                        for (int i = 0; i < 16; i++) {
                            temp += " ";
                        }

                        bookManage.setBorrowInfo(temp);

                        String s = bookManage.index + bookManage.getName() + bookManage.getBorrowInfo() + bookManage.getReserveInfo();

                        // book 파일 수정
                        BufferedReader fin = null;
                        fin = new BufferedReader(new FileReader(bookManage.book_fn));

                        String line = "";
                        String allLine;
                        String tempLine = "";
                        byte[] allbyte;

                        // index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

                        while ((allLine = fin.readLine()) != null) {
                            for (int i = 0; i < allLine.length(); i += bookManage.recordSize) {
                                line = allLine.substring(i, i + bookManage.recordSize);
                                if (bookManage.index.compareTo(line.substring(0, 5)) == 0) {
                                    tempLine += s;
                                } else {
                                    tempLine += line;
                                }
                            }

                        }
                        fin.close();

                        FileOutputStream out = new FileOutputStream(bookManage.book_fn);
                        allbyte = tempLine.getBytes();
                        out.write(allbyte);
                        out.close();

                    } catch (IOException e) {
                        System.out.println("File Open Error!!!");
                    }

                    System.out.println(bookManage.getName()+" 반납 완료");
                    bookManage.lentStr="";


                } else {
                    System.out.println("해당 책이 없습니다.");
                    return ;
                }
                System.out.print("Continue?(y/n) ");
                String ans;
                ans = input.nextLine();
                if (ans.compareTo("y") != 0)
                    cont = false;

            }
        }

    }

    //예약
    void UserReserve() {

        //이용자 확인
        memberManage.Confirm();

        //이미 예약중인지 확인
        String temp ="";
        for (int i = 0; i < 10; i++) {
            temp += " ";
        }

        if ((memberManage.getReserveBook()).compareTo(temp) == 0) {

            //예약할 책 인덱스 입력
            Book book = new Book();
            System.out.print("예약하실 ");
            bookManage.Confirm(true);

            if (!bookManage.success) {
                System.out.println("등록되지 않은 책입니다.");
                return ;
            }

            temp ="";
            //이미 예약된 책입니다.
            for (int i = 0; i < 10; i++) {
                temp += " ";
            }

            if (bookManage.getReserveInfo().compareTo(temp) != 0) {
                System.out.println("이미 예약된 책입니다.");
                return ;
            }

            //예약정보 y로 수정
            input = new Scanner(System.in);
            System.out.println("예약하시겠습니까?(y/n) : ");
            String ans = "";
            ans = input.nextLine();
            if (ans.compareTo("y") == 0) {
                try {
                    int len = 0;

                    bookManage.setReserveInfo("y");
                    len = bookManage.getReserveInfo().length();

                    for (int i = len; i < 10; i++) { //예약 크기 맞춰주기
                        bookManage.setReserveInfo(bookManage.getReserveInfo() + " ");
                    }

                    String s = bookManage.index + bookManage.getName() + bookManage.getBorrowInfo() + bookManage.getReserveInfo();

                    // book 파일 수정
                    BufferedReader fin = null;
                    fin = new BufferedReader(new FileReader(bookManage.book_fn));

                    String line = "";
                    String allLine;
                    String tempLine = "";
                    byte[] allbyte;

                    // index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

                    while ((allLine = fin.readLine()) != null) {
                        for (int i = 0; i < allLine.length(); i += bookManage.recordSize) {
                            line = allLine.substring(i, i + bookManage.recordSize);
                            if (bookManage.index.compareTo(line.substring(0, 5)) == 0) {
                                tempLine += s;
                            } else {
                                tempLine += line;
                            }
                        }

                    }
                    fin.close();

                    FileOutputStream out = new FileOutputStream(bookManage.book_fn);
                    allbyte = tempLine.getBytes();
                    out.write(allbyte);
                    out.close();

                    len = 0;

                    memberManage.setReserveBook(bookManage.index);
                    len = memberManage.getReserveBook().length();

                    for (int i = len; i < 10; i++) {
                        memberManage.setReserveBook(memberManage.getReserveBook() + " ");
                    }

                    s = memberManage.getId() + memberManage.getName() + memberManage.getStatus() + memberManage.getPhone() + memberManage.getReserveBook();
                    fin = new BufferedReader(new FileReader(memberManage.user_fn));

                    //변수들 초기화
                    line = "";
                    allLine = "";
                    tempLine = "";
                    allbyte = null;

                    // id=8, name=10, status=10, phone=11, reservebook=10(인덱스);
                    while ((allLine = fin.readLine()) != null) {
                        for (int i = 0; i < allLine.length(); i += memberManage.recordSize) {
                            line = allLine.substring(i, i + memberManage.recordSize);
                            if (memberManage.getId().compareTo(line.substring(0, 8)) == 0) {
                                tempLine += s;
                            } else {
                                tempLine += line;
                            }
                        }

                    }
                    fin.close();

                    out = new FileOutputStream(memberManage.user_fn);
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
