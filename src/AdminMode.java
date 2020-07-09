public class AdminMode {

    static java.util.Scanner in = null;
    MemberDataManage memberManage = new MemberDataManage();
    BookDataManage bookManage=new BookDataManage();
    UserMode userMode = new UserMode();

    //관리자 대출
    void AdminRental() {

        while (true) {
            int menu_no;
            in = new java.util.Scanner(System.in);
            System.out.println("*********************");
            System.out.println("이용자 리스트 보기 ==> 1");
            System.out.println("도서리스트 보기   ==> 2");
            System.out.println("대출하기         ==> 3");
            System.out.println("돌아가기         ==> 4");
            System.out.println("*********************");
            System.out.print("input menu number :");
            menu_no = in.nextInt();

            MemberDataManage memberManage = new MemberDataManage();
            BookDataManage bookManage = new BookDataManage();

            switch (menu_no) {
                case 1:
                    memberManage.showList();
                    break;
                case 2:
                    bookManage.showList(true);
                    break;
                case 3:
                    userMode.UserRental();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("메뉴 번호를 다시 입력하세요");
            }
        }
    }

    //관리자 반납
    void AdminTurnIn() {
        while (true) {
            int menu_no;
            in = new java.util.Scanner(System.in);
            System.out.println("***********************");
            System.out.println("이용자 리스트 보기 ==> 1");
            System.out.println("도서리스트 보기    ==> 2");
            System.out.println("반납하기          ==> 3");
            System.out.println("돌아가기          ==> 4");
            System.out.println("************************");
            System.out.print("input menu number :");
            menu_no = in.nextInt();

            MemberDataManage memberManage = new MemberDataManage();
            BookDataManage bookManage = new BookDataManage();


            switch (menu_no) {
                case 1:
                    memberManage.showList();
                    break;
                case 2:
                    bookManage.showList(true);
                    break;
                case 3:
                    userMode.UserTurnIn();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("메뉴 번호를 다시 입력하세요");
            }
        }

    }

    //관리자 예약
    void AdminReserve() {
        while (true) {
            int menu_no;
            in = new java.util.Scanner(System.in);
            System.out.println("**********************");
            System.out.println("이용자 리스트 보기 ==> 1");
            System.out.println("도서리스트 보기    ==> 2");
            System.out.println("예약하기          ==> 3");
            System.out.println("돌아가기          ==> 4");
            System.out.println("***********************");
            System.out.print("input menu number :");
            menu_no = in.nextInt();

            switch (menu_no) {
                case 1:
                    memberManage.showList();
                    break;
                case 2:
                    bookManage.showList(true);
                    break;
                case 3:
                    userMode.UserReserve();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("메뉴 번호를 다시 입력하세요");
            }
        }

    }
}
