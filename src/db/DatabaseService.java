package src.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseService {

    /**
     * 사용자 역할 ID 가져오기
     * @param username
     * @param password
     * @return role_id (로그인 성공 시). -1 (실패시)
     */
    public int getUserRoleId(String username, String password) {
        String query = "SELECT role_id FROM Employees WHERE username = ? AND password_hash = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("로그인 실패: 사용자 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // 인증 실패
    }

    /**
     * 비밀번호 해싱 (SHA-256)
     * @param password
     * @return
     */

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
