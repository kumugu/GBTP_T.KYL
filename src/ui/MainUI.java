package src.ui;

import src.ui.inventyory.InventoryUI;
import src.ui.login.LoginUI;
import src.ui.login.RegisterUI;
import src.ui.manager.MangerUI;
import src.ui.sales.ProductManagementUI;
import src.ui.sales.SalesUI;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {

    private static final String LOGIN_PANEL = "로그";
    private static final String REGISTER_PANEL = "직원 등록";
    private static final String LOBBY_PANEL = "로비";
    private static final String SALES_PANEL = "판매";
    private static final String PRODUCTS_PANEL = "상품 관리";
    private static final String INVENTORY_PANEL = "재고 및 주문";
    private static final String MANAGER_PANEL = "관리자 메뉴";

    private final JPanel centerPanel;

    public MainUI(){
        // 기본설정
        setTitle("매장 관리 프로그램");
        setSize(1000,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메뉴바 생성
        createMenuBar();

        // 메인 콘텐츠 패널 설정
        JPanel contentPane = new JPanel(new BoxLayout());
        setContentPane(contentPane);

        // 중앙 패널 생성 및 추가
        centerPanel = new JPanel(new CardLayout());
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // 패널 추가
        centerPanel.add(new LoginUI(this), LOGIN_PANEL);
        centerPanel.add(new RegisterUI(this), REGISTER_PANEL);
        centerPanel.add(new LobbyUI(this), LOBBY_PANEL);
        centerPanel.add(new SalesUI(this), SALES_PANEL);
        centerPanel.add(new ProductManagementUI(this), PRODUCTS_PANEL);
        centerPanel.add(new InventoryUI(this), INVENTORY_PANEL);
        centerPanel.add(new MangerUI(this), MANAGER_PANEL);

        // 초기 화면 설정
        showPanel(LOGIN_PANEL);

        // 화면 표시
        setVisible(true);
    }

    private void createMenuBar() {
    }
}
