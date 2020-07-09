import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {
    static Scanner input = null;

    public static void main(String[] args) {

        input = new java.util.Scanner(System.in);
        String passwd = "";
        String inputpass = "";
        int no;
        System.out.print("사용자 모드 => 1 , 관리자 모드 => 2 : ");
        no = input.nextInt();

        if (no == 1) {
            while (UserMenu())
                ;
        } else if (no == 2) {
            // 관리자 로그인
            try {
                System.out.print("관리자 비밀번호를 입력하세요 : ");
                inputpass = input.next();
                BufferedReader fin = null;
                fin = new BufferedReader(new FileReader("passwd.txt"));
                String tmp = "";
                while ((tmp = fin.readLine()) != null) {
                    passwd = tmp;
                    //System.out.println(passwd);
                }
                fin.close();

                if (passwd.compareTo(inputpass) == 0) {
                    while (AdminMenu())
                        ;
                } else {
                    System.out.println("관리자모드 로그인 실패");
                }

            } catch (FileNotFoundException e) {
                e.getStackTrace();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
        input.close();
    }

    static boolean UserMenu() {
        int menu_no;
        boolean ret = true;

        input = new Scanner(System.in);

        System.out.println("*******************");
        System.out.println("1. 도서    조회     ");
        System.out.println("2. 대          출  ");
        System.out.println("3. 반          납  ");
        System.out.println("4. 예          약  ");
        System.out.println("5. 이용자 정보      ");
        System.out.println("6. 종          료  ");
        System.out.println("*******************");
        System.out.print("input menu number : ");
        menu_no = input.nextInt();

        UserMode userMode = new UserMode();
        BookDataManage bookManage = new BookDataManage();
        MemberDataManage memberManage = new MemberDataManage();

        if (menu_no == 6) {
            ret = false;
        } else {
            if (menu_no >= 1 && menu_no <= 5)
                switch (menu_no) {

                    case 1:
                        bookManage.showList(false);
                        break;
                    case 2:
                        userMode.UserRental();
                        break;
                    case 3:
                        userMode.UserTurnIn();
                        break;
                    case 4:
                        userMode.UserReserve();
                        break;
                    case 5:
                        memberManage.MemberInfo();
                        break;
                }
            else
                System.out.println("메뉴 번호를 다시 입력하세요");
            ret = true;
        }

        return ret;
    }

    static boolean AdminMenu() {
        int menu_no = 0;
        boolean ret = true;
        input = new java.util.Scanner(System.in);

        System.out.println("********************");
        System.out.println("1. 대           출   ");
        System.out.println("2. 반           납   ");
        System.out.println("3. 예           약   ");
        System.out.println("4. 책 목록 관리       ");// 책 추가 폐기 관리
        System.out.println("5. 이용자  관리       ");
        System.out.println("6. 종           료   ");
        System.out.println("********************");
        System.out.print("input menu number : ");

        menu_no = input.nextInt();

        AdminMode adminMode = new AdminMode();
        MemberDataManage memberManage = new MemberDataManage();
        BookDataManage bookManage = new BookDataManage();

        if (menu_no == 6) {
            ret = false;
        } else {
            if (menu_no >= 1 && menu_no <= 5)
                switch (menu_no) {
                    case 1:
                        adminMode.AdminRental();
                        break;
                    case 2:
                        adminMode.AdminTurnIn();
                        break;
                    case 3:
                        adminMode.AdminReserve();
                        break;
                    case 4:
                        bookManage.AdminBookManage();
                        break;
                    case 5:
                        memberManage.AdminUserManage();
                        break;
                }
            else
                System.out.println("메뉴번호를 다시 입력하세요");
            ret = true;
        }
        return ret;
    }


}

