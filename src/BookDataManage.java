import java.io.*;
import java.util.Scanner;

public class BookDataManage extends Book{
    static java.util.Scanner input = null;
    String book_fn = "book.bin";
    int lentCount = 0;
    String lentStr="";
    String index = "";
    boolean success = false;
    final int recordSize = 51;

    BookDataManage(){
        super();
    }

    void AdminBookManage() {

        showList(true);

        input = new java.util.Scanner(System.in);
        int no;

        System.out.print("도서 추가 => 1 , 도서 삭제  => 2 , 돌아가기 => 3 : ");
        no = input.nextInt();

        if (no == 1) {
            addItem();
        } else if (no == 2) {
            eraseItem();
        } else if (no == 3) {
            ;
        }

    }

    //책 파일 읽기
    void fileRead(String option) {

        Book book = new Book();

        String ans = "";
        try {
            BufferedReader fin = null;
            fin = new BufferedReader(new FileReader(book_fn));
            String line = "";
            String allLine;
            // index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10

            while ((allLine = fin.readLine()) != null) {
                for (int i = 0; i < allLine.length(); i += recordSize) {
                    line = allLine.substring(i, i + recordSize);

                    switch (option) {

                        case "list":
                            System.out.println(line.substring(0, 5) + " " + line.substring(5, 25) + " "
                                    + line.substring(33, 41) + "       " + line.substring(41, 51));
                            break;
                        case "adminlist":
                            System.out.println(line.substring(0, 5) + " " + line.substring(5, 25) + " "
                                    + line.substring(25, 41) + "       " + line.substring(41, 51));
                            break;
                        // index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10
                        case "conindex":
                            if (index.compareTo(line.substring(0, 5)) == 0) {
                                index = line.substring(0, 5);
                                setName(line.substring(5, 25));
                                setBorrowInfo(line.substring(25, 41));
                                setReserveInfo(line.substring(41, 51));
                                success = true;
                                break;
                            }
                            break;

                        case "conname":
                            if (getName().compareTo(line.substring(5, 25)) == 0) {
                                index = line.substring(0, 5);
                                setName(line.substring(5, 25));
                                setBorrowInfo(line.substring(25, 41));
                                setReserveInfo(line.substring(41, 51));
                                success = true;
                                break;
                            }
                            break;
                        case "lent":
                            index = line.substring(0, 5);
                            book.setName(line.substring(5, 25));
                            book.setBorrowInfo(line.substring(25, 41));
                            book.setReserveInfo(line.substring(41, 51));
                            success = true;
                            break;
                    }

                    // 특정 id 찾기
                    if (option.substring(0, 4).compareTo("lent") == 0) {
                        if (option.substring(4, 12).compareTo(line.substring(25, 33)) == 0) {
                            lentCount++;
                        }
                    }
                    if(option.length()>=8) { //대출중인 책 목록 출력
                        if (option.substring(0, 8).compareTo("lentbook") == 0) {
                            if (option.substring(8, 16).compareTo(line.substring(25, 33)) == 0) {
                                System.out.println(
                                        line.substring(0, 5) + " " + line.substring(5, 25) + " " + line.substring(33, 41));
                                lentStr+=line.substring(0, 5);
                            }
                        }
                    }
                }
            }

            fin.close();

        } catch (FileNotFoundException e) {
            //System.out.println(e);
            System.out.println("현재 등록된 도서가 존재하지 않습니다.");
            System.out.println("도서 추가하기");
            addItem();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    void showList(boolean ad) {

        if (ad == false) {
            System.out.println("< 도서 목록 >");
            System.out.println("==============================================================");
            System.out.println("인덱스   제목                   대출정보                예약정보   ");
            System.out.println("==============================================================");
            fileRead("list");
        }
        else if (ad == true) {
            System.out.println("< 도서 목록 >");
            System.out.println("==============================================================");
            System.out.println("인덱스   제목                   대출정보                예약정보   ");
            System.out.println("==============================================================");
            fileRead("adminlist");
        }
        System.out.println("");
        int search_no;
        System.out.print("제목으로 검색하기 => 1 검색 안함 => 2 : ");
        input = new Scanner(System.in);
        search_no = input.nextInt();
        if(search_no==1) {
            Search();
        }
        else
            return ;
    }

    void addItem() {
        try {
            FileOutputStream out = new FileOutputStream(book_fn, true);
            boolean cont = true;
            input = new java.util.Scanner(System.in);
            int len = 0;
            byte[] oneLine = new byte[recordSize];
            // index=5, bookname=20, 대출(대출자, 반납일)=16, 예약=10
            while (cont) {
                System.out.println("도서번호 : ");
                index = input.nextLine();
                /////////인덱스 겹치는지 확인하고 저장
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
                String tempName = input.nextLine();
                setName(tempName);
                for (int i = 0; i < recordSize; i++) {
                    oneLine[i] = 0;
                }
                len = index.length();
                for (int i = len; i < 5; i++) {
                    index = index + " ";
                }
                len = getName().length();
                for (int i = len; i < 20; i++) {
                    setName(getName() + " ");
                }
                for (int i = 0; i < 26; i++) { // 대출,예약 칸 만들어주기
                    setName(getName() + " ");
                }

                index = index + getName();
                oneLine = index.getBytes();

                out.write(oneLine);
                System.out.print("Continue?(y/n) ");
                String ans = input.nextLine();
                if (ans.compareTo("y") != 0)
                    cont = false;
            }

            out.close();

        } catch (IOException e) {
            System.out.println("File Open Error!!!");
        }

    }

    void eraseItem() {
        input = new java.util.Scanner(System.in);
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
        if((getBorrowInfo()).compareTo(temp)==0&&(getReserveInfo()).compareTo(temp2)==0) {
            //대출중 아니면
            System.out.println("정말 삭제하시겠습니까?(y/n) : ");
            ans = input.nextLine();
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

    void Search() {
        int len = 0;
        System.out.print("도서 제목을 입력하세요 : ");
        input = new java.util.Scanner(System.in);
        String tempName = input.nextLine();
        setName(tempName);

        len = getName().length();
        for (int i = len; i < 20; i++) {
            setName(getName() + " ");
        }

        fileRead("conname");
        if (success == true) {
            bookinfo();
        }
    }

    void Confirm(boolean print) {
        int len = 0;
        System.out.print("도서 index를 입력하세요 : ");
        input = new Scanner(System.in);
        index = input.nextLine();
        len = index.length();
        for (int i = len; i < 5; i++) {
            index = index + " ";
        }

        fileRead("conindex");

        if (print && success) {
            bookinfo();
        }

    }

    void bookinfo() {
        System.out.println("도서 제목 : " + getName());
        System.out.println("대출상태 : " + getBorrowInfo().substring(8));
        System.out.println("예약상태 : " + getReserveInfo());
    }

}
