package src.ui;

import src.ui.login.LoginUI;
import src.ui.login.RegisterUI;
import src.ui.sales.SalesUI;
import src.ui.sales.ProductManagementUI;
import src.ui.inventyory.InventoryUI;
import src.ui.manager.MangerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.SQLException;

public class MainUI extends JFrame {

    public static final String LOGIN_PANEL = "로그";
    public static final String REGISTER_PANEL = "직원 등록";
    public static final String LOBBY_PANEL = "로비";
    private static final String SALES_PANEL = "판매";
    private static final String PRODUCTS_PANEL = "상품 관리";
    private static final String INVENTORY_PANEL = "재고 및 주문";
    private static final String MANAGER_PANEL = "관리자 메뉴";

    private final JPanel centerPanel;   // 화면 전환을 관리할 중앙 패널
    private boolean isLoggedIn = false;  // 로그인 상태를 저장하는 변수
    private Integer currentUserRoleId = null;   // role_id 저장

    public MainUI() throws SQLException{
        // 기본설정
        setTitle("매장 관리 프로그램");
        setSize(1000,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메뉴바 생성
        createMenuBar();

        // 메인 콘텐츠 패널 설정
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // 중앙 패널 생성 및 추가
        centerPanel = new JPanel(new CardLayout());
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // 패널 추가
        centerPanel.add(new LoginUI(this), LOGIN_PANEL);
        centerPanel.add(new RegisterUI(this), REGISTER_PANEL);
        centerPanel.add(new LobbyUI(this), LOBBY_PANEL);
        centerPanel.add(new SalesUI(), SALES_PANEL);
        centerPanel.add(new ProductManagementUI(), PRODUCTS_PANEL);
        centerPanel.add(new InventoryUI(), INVENTORY_PANEL);
        centerPanel.add(new MangerUI(), MANAGER_PANEL);

        // 초기 화면 설정
        showPanel(LOGIN_PANEL);

        // 화면 표시
        setVisible(true);
    }

    /**
     * 패널 전환 메서드 - cardLayout을 사용하여 전환
     * @param panelName
     */
    public static void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (centerPanel.getLayout());

        // RegisterUI 패널 초기화
        if (panelName.equals(REGISTER_PANEL)) {
            Component[] components = centerPanel.getComponents();
            for (Component component : components) {
                if (component instanceof  RegisterUI) {
                    RegisterUI registerUI = (RegisterUI) component;
                    registerUI.clearFields(); // 텍스트 필드 초기화
                    break;
                }
            }
        }

        // LoginUI 패널 초기화
        if (panelName.equals(LOGIN_PANEL)) {
            Component[] components = centerPanel.getComponents();
            for (Component component : components) {
                if (component instanceof LoginUI) {
                    LoginUI loginUI = (LoginUI) component;
                    loginUI.clearFields(); // 텍스트 필드 초기화
                    break;
                }
            }
        }
        // 패널 전환
        cl.show(centerPanel, panelName);
    }


    /**
     * 메뉴바 생성 - 홈, 파일, 업무, 도움말
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 홈 메뉴
        JMenu homeMenu = new JMenu("🏠홈");
        homeMenu.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (isUserLoggedIn()) {
                    EventManager.getInstance().notifyListeners("REFRESH_UI");
                    showPanel(LOBBY_PANEL); // "홈" 클릭 시 로비 화면으

                } else {
                    JOptionPane.showMessageDialog(null, "먼저 로그인 해주세요.");
                    showPanel(LOBBY_PANEL); // 로그인 화면으로 이동
                }
            }
        });

        // 파일 메뉴
        JMenu fileMenu = new JMenu("파일");
        JMenuItem printItem = new JMenuItem("인쇄");
        printItem.addActionListener(e -> printCurrentPanel()); // 인쇄 가능
        JMenuItem logoutItem = new JMenuItem("로그아웃");
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog
                    (null, "로그아웃하시겠습니까?",
                            "로그아웃 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_NO_OPTION) {
                isLoggedIn = false; // 로그인 상태 해제
                JOptionPane.showMessageDialog
                        (null, "로그아웃되었습니다.");
                showPanel(LOGIN_PANEL); // 로그인 화면으로 이동
            }
        });

        JMenuItem exitItem = new JMenuItem("종료");
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog
                    (null, "종료하시겠습니까?",
                            "종료 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_NO_OPTION) {
                System.exit(0); // 애플리케이션 종료
            }
        });

        fileMenu.add(printItem);
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // 업무 메뉴
        JMenu workMenu = new JMenu("업무");
        JMenuItem salesItem = new JMenuItem("판매 관리");
        salesItem.addActionListener(e -> {
            if (isUserLoggedIn()) {
                EventManager.getInstance().notifyListeners(EventTypes.REFRESH_UI);
                showPanel(PRODUCTS_PANEL);
            } else {
                JOptionPane.showMessageDialog(null, "먼저 로그인해주세요.");
                showPanel(LOGIN_PANEL);
            }
        });

        JMenuItem productItem = new JMenuItem("상품 관리");
        productItem.addActionListener(e -> {
            if (isUserLoggedIn()) {
                EventManager.getInstance().notifyListeners(EventTypes.REFRESH_UI);
            } else {
                JOptionPane.showMessageDialog(null, "먼저 로그인해주세요.");
                showPanel(LOGIN_PANEL);
            }
        });

        JMenuItem inventoryItem = new JMenuItem("재고 및 주문");
        inventoryItem.addActionListener(e -> {
            if (isUserLoggedIn()) {
                EventManager.getInstance().notifyListeners(EventTypes.REFRESH_UI);
                showPanel(INVENTORY_PANEL);
            } else {
                JOptionPane.showMessageDialog(null, "먼저 로그인해주세요.");
                showPanel(LOGIN_PANEL);
            }
        });

        JMenuItem managerItem = new JMenuItem("관리자 메뉴");
        managerItem.addActionListener(e -> {
            if (!isUserLoggedIn()) {
                JOptionPane.showMessageDialog(null, "먼저 로그인해주세요.");
                showPanel(LOGIN_PANEL);
            } else {
                JOptionPane.showMessageDialog
                        (null, "관리자 메뉴는 매니저만 접근 가능합니다.",
                                "접근 제한", JOptionPane.WARNING_MESSAGE);
            }
        });

        workMenu.add(salesItem);
        workMenu.add(productItem);
        workMenu.add(inventoryItem);
        workMenu.add(managerItem);

        // 메뉴바에 메뉴 추가
        menuBar.add(homeMenu);
        menuBar.add(fileMenu);
        menuBar.add(workMenu);

        // 메뉴바 설정
        setJMenuBar(menuBar);
    }

    // 프린트 관련 메서드
    private void printCurrentPanel() {
        PrintJob job = PrintJob.getPrinterJob();
        job.setJobName("현재 화면 출력");

        job.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat, int pageIdex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                centerPanel.printAll(g2d);
                return PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * role_id 저장 로그인 성공 시
     */
    public void loginSuccess(int userRoleId) {
        isLoggedIn = true;
        currentUserRoleId = userRoleId;
    }


    /**
     * 사용자 로그인 여부 확인 메서드
     * @return - true: 로그인, false: 로그인 되지 않음
     */
    public boolean isUserLoggedIn() {
        return isLoggedIn;
    }


    /**
     * 메인 메서드 - 애플리케이션 실
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()  -> {

        });
    }
}
