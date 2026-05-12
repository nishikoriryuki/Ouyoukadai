package dao;

import model.User;

public class TestDAO {

    public static void main(String[] args) {

        UserDAO dao = new UserDAO();

        User user =
                dao.chooseRandomUser();

        if (user != null) {

            System.out.println(
                    "抽選成功"
            );

            System.out.println(
                    "名前: " +
                    user.getName()
            );

            System.out.println(
                    "趣味: " +
                    user.getHobby()
            );

            System.out.println(
                    "レア度: " +
                    user.getRarity()
            );

        } else {

            System.out.println(
                    "データ取得失敗"
            );
        }
    }
}