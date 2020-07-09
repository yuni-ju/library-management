import java.io.*;
import java.util.Scanner;

public class MemberDataManage extends Member implements DataManage {

    static Scanner input = null;
    final int recordSize = 49;
    String user_fn = "memberInfo.bin";
    byte[] oneLine = new byte[recordSize];
    String dateStr;
    int returnDay;
    int borrowNum;
    boolean success = false;

    //사용자 관리
    void AdminUserManage() {
        showList();

        input = new Scanner(System.in);
        int no;

        System.out.print("사용자 추가 => 1 , 사용자 삭제  => 2 , 돌아가기 => 3 : ");
        no = input.nextInt();

        if (no == 1) {
            addItem();
        } else if (no == 2) {
            eraseItem();
        } else if (no == 3) {
            ;
        }
    }

    //사용자 정보 + 대출정보
    void MemberInfo() {
        BookDataManage bookManage = new BookDataManage();
        Confirm();
        if (success == true) {
            System.out.println("ID : " + getId());
            System.out.println("이름 : " + getName());
            System.out.println("구분 : " + getStatus());
            System.out.println("전화번호 : " + getPhone());
            System.out.println("예약 정보 : " + getReserveBook());
            System.out.println("대출 정보: ");
            String lentbook = "lentbook" + getId();
            System.out.println("================================================");
            System.out.println( "인덱스         제목                정상반납일      ");
            System.out.println("================================================");
            bookManage.fileRead(lentbook);
            System.out.println("");
        }
    }

    //회원 파일 읽기
    public void fileRead(boolean print) {
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
                        System.out.printf("%8s %10s %10s %11s                 %10s\n",
                                line.substring(0, 8), line.substring(8, 18), line.substring(18, 28),line.substring(28, 39),line.substring(39, 49));

                    } else if (getId().compareTo(line.substring(0, 8)) == 0) {
                        setId(line.substring(0, 8));
                        setName(line.substring(8, 18));
                        setStatus( line.substring(18, 28));
                        setPhone(line.substring(28, 39));
                        setReserveBook(line.substring(39, 49));
                        success = true;
                        break;
                    }
                }
            }

            fin.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //사용자 추가
    public void addItem() {
        try {
            FileOutputStream out = new FileOutputStream(user_fn, true);
            boolean cont = true;
            input = new Scanner(System.in);

            int no = 0;

            int len = 0;
            // id=8, name=10, status=10, phone=11, reserveBook=10(인덱스);
            while (cont) {
                System.out.println("ID : ");
                String tempId = input.nextLine();
                setId(tempId);

                len = getId().length();
                for (int i = len; i < 8; i++) {
                    setId(getId() + " ");
                }

                fileRead(false);
                if (success == true) {
                    System.out.println("이미 존재하는 ID입니다.");
                    return ;
                }

                System.out.println("이름 : ");
                String tempName = input.nextLine();
                setName(tempName);
                System.out.println("학생 => 1 교수 =>2 : "); // 입력시 한글 깨지는 문제..
                no = Integer.parseInt(input.nextLine());
                if (no == 1) {
                    setStatus("학부생");
                } else if (no == 2) {
                    setStatus("교수");
                }
                System.out.println("전화번호 : ");
                String tempPhone = input.nextLine();
                setPhone(tempPhone);

                for (int i = 0; i < recordSize; i++) {
                    oneLine[i] = 0;
                }
                len = getId().length();
                for (int i = len; i < 8; i++) {
                    setId(getId() + " ");
                }
                len = getName().length();
                for (int i = len; i < 10; i++) {
                    setName(getName() + " ");
                }
                len = getStatus().length();
                for (int i = len; i < 10; i++) {
                    setStatus(getStatus() + " ");
                }
                len = getPhone().length();
                for (int i = len; i < 11; i++) {
                    setPhone(getPhone() + " ");
                }
                for (int i = 0; i < 10; i++) { // 예약 칸 만들어주기
                    setPhone(getPhone() + " ");
                }

                setId(getId() + getName() + getStatus() + getPhone());
                oneLine = getId().getBytes();

                out.write(oneLine);
                System.out.print("Continue?(y/n) ");
                String ans;
                ans = input.nextLine();
                if (ans.compareTo("y") != 0)
                    cont = false;
            }

            out.close();

        } catch (IOException e) {
            System.out.println("File Open Error!!!");
        }

    }

    //사용자 삭제
    public void eraseItem() {
        input = new Scanner(System.in);
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

        BookDataManage bookManage = new BookDataManage();

        String conlent = "lent"+getId();
        bookManage.fileRead(conlent);
        if(bookManage.lentCount == 0)
        {
            delete = true;
        }

        if (delete == true && (getReserveBook()).compareTo(temp2) == 0) {
            // 대출중 아니면
            System.out.println("정말 삭제하시겠습니까?(y/n) : ");
            ans = input.nextLine();
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
                            if (getId().compareTo(line.substring(0, 8)) != 0) {
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

    //사용자 리스트
    public void showList() {

        System.out.println("< 사용자 목록 >");
        System.out.println("===========================================================================");
        System.out.println("ID        이름           구분           전화번호                예약 정보      ");
        System.out.println("===========================================================================");
        fileRead(true);
        System.out.println("");

    }

    //사용자 확인
    String Confirm() {
        int len = 0;
        System.out.print("ID를 입력하세요 : ");
        input = new java.util.Scanner(System.in);
        String tempId = input.nextLine();
        setId(tempId);
        // 글자 크기 맞춰주기
        len = getId().length();

        fileRead(false);

        if (success == false) {
            System.out.println("등록되지 않은 ID입니다.");
        }

        return getId();
    }

    void UserClassify() {

        Member mem = null;
        String s = getStatus().trim();

        switch(s)
        {
            case "학부생":
                mem= new Student();
                break;
            case "교수":
                mem = new Professor();
                break;
        }
        returnDay = mem.ReturnDays();
        borrowNum = mem.BorrowNum();

    }
}
