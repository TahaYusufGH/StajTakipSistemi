import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
// Excel export için Apache POI kullanılacak
// Eğer POI yoksa CSV export kullanılabilir

public class Main extends JFrame {
    private JTextField nameField;
    private JTextField startDateField;
    private JTextField searchField;
    private JTable stageTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private List<String> stageNames;
    private List<Integer> stageDurations; // Gün cinsinden süreler
    
    // Türkiye Resmi Tatilleri (2025-2040)
    private List<LocalDate> holidays = Arrays.asList(
        // 2025 Yılı
        LocalDate.of(2025, 1, 1),   // Yılbaşı
        LocalDate.of(2025, 3, 30),  // Ramazan Bayramı 1. gün
        LocalDate.of(2025, 3, 31),  // Ramazan Bayramı 2. gün
        LocalDate.of(2025, 4, 1),   // Ramazan Bayramı 3. gün
        LocalDate.of(2025, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2025, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2025, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2025, 6, 6),   // Kurban Bayramı 1. gün
        LocalDate.of(2025, 6, 7),   // Kurban Bayramı 2. gün
        LocalDate.of(2025, 6, 8),   // Kurban Bayramı 3. gün
        LocalDate.of(2025, 6, 9),   // Kurban Bayramı 4. gün
        LocalDate.of(2025, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2025, 8, 30),  // Zafer Bayramı
        LocalDate.of(2025, 10, 29), // Cumhuriyet Bayramı

        // 2026 Yılı
        LocalDate.of(2026, 1, 1),   // Yılbaşı
        LocalDate.of(2026, 3, 20),  // Ramazan Bayramı 1. gün
        LocalDate.of(2026, 3, 21),  // Ramazan Bayramı 2. gün
        LocalDate.of(2026, 3, 22),  // Ramazan Bayramı 3. gün
        LocalDate.of(2026, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2026, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2026, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2026, 5, 27),  // Kurban Bayramı 1. gün
        LocalDate.of(2026, 5, 28),  // Kurban Bayramı 2. gün
        LocalDate.of(2026, 5, 29),  // Kurban Bayramı 3. gün
        LocalDate.of(2026, 5, 30),  // Kurban Bayramı 4. gün
        LocalDate.of(2026, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2026, 8, 30),  // Zafer Bayramı
        LocalDate.of(2026, 10, 29), // Cumhuriyet Bayramı

        // 2027 Yılı
        LocalDate.of(2027, 1, 1),   // Yılbaşı
        LocalDate.of(2027, 3, 9),   // Ramazan Bayramı 1. gün
        LocalDate.of(2027, 3, 10),  // Ramazan Bayramı 2. gün
        LocalDate.of(2027, 3, 11),  // Ramazan Bayramı 3. gün
        LocalDate.of(2027, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2027, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2027, 5, 16),  // Kurban Bayramı 1. gün
        LocalDate.of(2027, 5, 17),  // Kurban Bayramı 2. gün
        LocalDate.of(2027, 5, 18),  // Kurban Bayramı 3. gün
        LocalDate.of(2027, 5, 19),  // Kurban Bayramı 4. gün & Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2027, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2027, 8, 30),  // Zafer Bayramı
        LocalDate.of(2027, 10, 29), // Cumhuriyet Bayramı

        // 2028 Yılı
        LocalDate.of(2028, 1, 1),   // Yılbaşı
        LocalDate.of(2028, 2, 26),  // Ramazan Bayramı 1. gün
        LocalDate.of(2028, 2, 27),  // Ramazan Bayramı 2. gün
        LocalDate.of(2028, 2, 28),  // Ramazan Bayramı 3. gün
        LocalDate.of(2028, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2028, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2028, 5, 5),   // Kurban Bayramı 1. gün
        LocalDate.of(2028, 5, 6),   // Kurban Bayramı 2. gün
        LocalDate.of(2028, 5, 7),   // Kurban Bayramı 3. gün
        LocalDate.of(2028, 5, 8),   // Kurban Bayramı 4. gün
        LocalDate.of(2028, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2028, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2028, 8, 30),  // Zafer Bayramı
        LocalDate.of(2028, 10, 29), // Cumhuriyet Bayramı

        // 2029 Yılı
        LocalDate.of(2029, 1, 1),   // Yılbaşı
        LocalDate.of(2029, 2, 14),  // Ramazan Bayramı 1. gün
        LocalDate.of(2029, 2, 15),  // Ramazan Bayramı 2. gün
        LocalDate.of(2029, 2, 16),  // Ramazan Bayramı 3. gün
        LocalDate.of(2029, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2029, 4, 26),  // Kurban Bayramı 1. gün
        LocalDate.of(2029, 4, 27),  // Kurban Bayramı 2. gün
        LocalDate.of(2029, 4, 28),  // Kurban Bayramı 3. gün
        LocalDate.of(2029, 4, 29),  // Kurban Bayramı 4. gün
        LocalDate.of(2029, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2029, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2029, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2029, 8, 30),  // Zafer Bayramı
        LocalDate.of(2029, 10, 29), // Cumhuriyet Bayramı

        // 2030 Yılı
        LocalDate.of(2030, 1, 1),   // Yılbaşı
        LocalDate.of(2030, 2, 4),   // Ramazan Bayramı 1. gün
        LocalDate.of(2030, 2, 5),   // Ramazan Bayramı 2. gün
        LocalDate.of(2030, 2, 6),   // Ramazan Bayramı 3. gün
        LocalDate.of(2030, 4, 13),  // Kurban Bayramı 1. gün
        LocalDate.of(2030, 4, 14),  // Kurban Bayramı 2. gün
        LocalDate.of(2030, 4, 15),  // Kurban Bayramı 3. gün
        LocalDate.of(2030, 4, 16),  // Kurban Bayramı 4. gün
        LocalDate.of(2030, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2030, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2030, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2030, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2030, 8, 30),  // Zafer Bayramı
        LocalDate.of(2030, 10, 29), // Cumhuriyet Bayramı

        // 2031 Yılı
        LocalDate.of(2031, 1, 1),   // Yılbaşı
        LocalDate.of(2031, 1, 24),  // Ramazan Bayramı 1. gün
        LocalDate.of(2031, 1, 25),  // Ramazan Bayramı 2. gün
        LocalDate.of(2031, 1, 26),  // Ramazan Bayramı 3. gün
        LocalDate.of(2031, 4, 2),   // Kurban Bayramı 1. gün
        LocalDate.of(2031, 4, 3),   // Kurban Bayramı 2. gün
        LocalDate.of(2031, 4, 4),   // Kurban Bayramı 3. gün
        LocalDate.of(2031, 4, 5),   // Kurban Bayramı 4. gün
        LocalDate.of(2031, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2031, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2031, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2031, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2031, 8, 30),  // Zafer Bayramı
        LocalDate.of(2031, 10, 29), // Cumhuriyet Bayramı

        // 2032 Yılı
        LocalDate.of(2032, 1, 1),   // Yılbaşı
        LocalDate.of(2032, 1, 12),  // Ramazan Bayramı 1. gün
        LocalDate.of(2032, 1, 13),  // Ramazan Bayramı 2. gün
        LocalDate.of(2032, 1, 14),  // Ramazan Bayramı 3. gün
        LocalDate.of(2032, 3, 22),  // Kurban Bayramı 1. gün
        LocalDate.of(2032, 3, 23),  // Kurban Bayramı 2. gün
        LocalDate.of(2032, 3, 24),  // Kurban Bayramı 3. gün
        LocalDate.of(2032, 3, 25),  // Kurban Bayramı 4. gün
        LocalDate.of(2032, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2032, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2032, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2032, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2032, 8, 30),  // Zafer Bayramı
        LocalDate.of(2032, 10, 29), // Cumhuriyet Bayramı

        // 2033 Yılı
        LocalDate.of(2033, 1, 1),   // Yılbaşı & Ramazan Bayramı 1. gün
        LocalDate.of(2033, 1, 2),   // Ramazan Bayramı 2. gün
        LocalDate.of(2033, 1, 3),   // Ramazan Bayramı 3. gün
        LocalDate.of(2033, 3, 11),  // Kurban Bayramı 1. gün
        LocalDate.of(2033, 3, 12),  // Kurban Bayramı 2. gün
        LocalDate.of(2033, 3, 13),  // Kurban Bayramı 3. gün
        LocalDate.of(2033, 3, 14),  // Kurban Bayramı 4. gün
        LocalDate.of(2033, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2033, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2033, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2033, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2033, 8, 30),  // Zafer Bayramı
        LocalDate.of(2033, 10, 29), // Cumhuriyet Bayramı

        // 2034 Yılı
        LocalDate.of(2034, 1, 1),   // Yılbaşı
        LocalDate.of(2034, 2, 28),  // Kurban Bayramı 1. gün
        LocalDate.of(2034, 3, 1),   // Kurban Bayramı 2. gün
        LocalDate.of(2034, 3, 2),   // Kurban Bayramı 3. gün
        LocalDate.of(2034, 3, 3),   // Kurban Bayramı 4. gün
        LocalDate.of(2034, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2034, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2034, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2034, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2034, 8, 30),  // Zafer Bayramı
        LocalDate.of(2034, 10, 29), // Cumhuriyet Bayramı
        LocalDate.of(2034, 12, 21), // Ramazan Bayramı 1. gün
        LocalDate.of(2034, 12, 22), // Ramazan Bayramı 2. gün
        LocalDate.of(2034, 12, 23), // Ramazan Bayramı 3. gün

        // 2035 Yılı
        LocalDate.of(2035, 1, 1),   // Yılbaşı
        LocalDate.of(2035, 2, 17),  // Kurban Bayramı 1. gün
        LocalDate.of(2035, 2, 18),  // Kurban Bayramı 2. gün
        LocalDate.of(2035, 2, 19),  // Kurban Bayramı 3. gün
        LocalDate.of(2035, 2, 20),  // Kurban Bayramı 4. gün
        LocalDate.of(2035, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2035, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2035, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2035, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2035, 8, 30),  // Zafer Bayramı
        LocalDate.of(2035, 10, 29), // Cumhuriyet Bayramı
        LocalDate.of(2035, 12, 10), // Ramazan Bayramı 1. gün
        LocalDate.of(2035, 12, 11), // Ramazan Bayramı 2. gün
        LocalDate.of(2035, 12, 12), // Ramazan Bayramı 3. gün

        // 2036 Yılı
        LocalDate.of(2036, 1, 1),   // Yılbaşı
        LocalDate.of(2036, 2, 6),   // Kurban Bayramı 1. gün
        LocalDate.of(2036, 2, 7),   // Kurban Bayramı 2. gün
        LocalDate.of(2036, 2, 8),   // Kurban Bayramı 3. gün
        LocalDate.of(2036, 2, 9),   // Kurban Bayramı 4. gün
        LocalDate.of(2036, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2036, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2036, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2036, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2036, 8, 30),  // Zafer Bayramı
        LocalDate.of(2036, 10, 29), // Cumhuriyet Bayramı
        LocalDate.of(2036, 11, 29), // Ramazan Bayramı 1. gün
        LocalDate.of(2036, 11, 30), // Ramazan Bayramı 2. gün
        LocalDate.of(2036, 12, 1),  // Ramazan Bayramı 3. gün

        // 2037 Yılı
        LocalDate.of(2037, 1, 1),   // Yılbaşı
        LocalDate.of(2037, 1, 26),  // Kurban Bayramı 1. gün
        LocalDate.of(2037, 1, 27),  // Kurban Bayramı 2. gün
        LocalDate.of(2037, 1, 28),  // Kurban Bayramı 3. gün
        LocalDate.of(2037, 1, 29),  // Kurban Bayramı 4. gün
        LocalDate.of(2037, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2037, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2037, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2037, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2037, 8, 30),  // Zafer Bayramı
        LocalDate.of(2037, 10, 29), // Cumhuriyet Bayramı
        LocalDate.of(2037, 11, 18), // Ramazan Bayramı 1. gün
        LocalDate.of(2037, 11, 19), // Ramazan Bayramı 2. gün
        LocalDate.of(2037, 11, 20), // Ramazan Bayramı 3. gün

        // 2038 Yılı
        LocalDate.of(2038, 1, 1),   // Yılbaşı
        LocalDate.of(2038, 1, 15),  // Kurban Bayramı 1. gün
        LocalDate.of(2038, 1, 16),  // Kurban Bayramı 2. gün
        LocalDate.of(2038, 1, 17),  // Kurban Bayramı 3. gün
        LocalDate.of(2038, 1, 18),  // Kurban Bayramı 4. gün
        LocalDate.of(2038, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2038, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2038, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2038, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2038, 8, 30),  // Zafer Bayramı
        LocalDate.of(2038, 10, 29), // Cumhuriyet Bayramı
        LocalDate.of(2038, 11, 7),  // Ramazan Bayramı 1. gün
        LocalDate.of(2038, 11, 8),  // Ramazan Bayramı 2. gün
        LocalDate.of(2038, 11, 9),  // Ramazan Bayramı 3. gün

        // 2039 Yılı
        LocalDate.of(2039, 1, 1),   // Yılbaşı
        LocalDate.of(2039, 1, 4),   // Kurban Bayramı 1. gün
        LocalDate.of(2039, 1, 5),   // Kurban Bayramı 2. gün
        LocalDate.of(2039, 1, 6),   // Kurban Bayramı 3. gün
        LocalDate.of(2039, 1, 7),   // Kurban Bayramı 4. gün
        LocalDate.of(2039, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2039, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2039, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2039, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2039, 8, 30),  // Zafer Bayramı
        LocalDate.of(2039, 10, 27), // Ramazan Bayramı 1. gün
        LocalDate.of(2039, 10, 28), // Ramazan Bayramı 2. gün
        LocalDate.of(2039, 10, 29), // Ramazan Bayramı 3. gün & Cumhuriyet Bayramı

        // 2040 Yılı
        LocalDate.of(2040, 1, 1),   // Yılbaşı
        LocalDate.of(2040, 4, 23),  // Ulusal Egemenlik ve Çocuk Bayramı
        LocalDate.of(2040, 5, 1),   // Emek ve Dayanışma Günü
        LocalDate.of(2040, 5, 19),  // Atatürk'ü Anma, Gençlik ve Spor Bayramı
        LocalDate.of(2040, 7, 15),  // Demokrasi ve Millî Birlik Günü
        LocalDate.of(2040, 8, 30),  // Zafer Bayramı
        LocalDate.of(2040, 10, 15), // Ramazan Bayramı 1. gün
        LocalDate.of(2040, 10, 16), // Ramazan Bayramı 2. gün
        LocalDate.of(2040, 10, 17), // Ramazan Bayramı 3. gün
        LocalDate.of(2040, 10, 29), // Cumhuriyet Bayramı
        LocalDate.of(2040, 12, 23), // Kurban Bayramı 1. gün
        LocalDate.of(2040, 12, 24), // Kurban Bayramı 2. gün
        LocalDate.of(2040, 12, 25), // Kurban Bayramı 3. gün
        LocalDate.of(2040, 12, 26)  // Kurban Bayramı 4. gün
    );
    
    private static final String DATA_FILE = "staj_verileri.txt";

    public Main() {
        initializeStages();
        setupUI();
        loadData(); // Uygulama açılırken verileri yükle
    }

    private void initializeStages() {
        stageNames = Arrays.asList(
            "Cumhuriyet Başsavcılığı",
            "Bölge İdare",
            "Ağır Ceza",
            "Asliye Ceza", 
            "Sulh Hukuk",
            "Asliye Hukuk/Asl. Tic/İş",
            "İcra",
            "Mazbata"
        );
        
        stageDurations = Arrays.asList(
            15,  // Cumhuriyet Başsavcılığı - 15 gün
            15,  // Bölge İdare - 15 gün
            30,  // Ağır Ceza - 1 ay
            30,  // Asliye Ceza - 1 ay
            15,  // Sulh Hukuk - 15 gün
            45,  // Asliye Hukuk/Asl. Tic/İş - 1.5 ay
            30,  // İcra - 1 ay
            2    // Mazbata - 2 gün
        );
    }

    private void setupUI() {
        setTitle("Staj Takip Uygulaması");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Üst panel - Form
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Orta panel - Tablo
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // Alt panel - Butonlar
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createTitledBorder("Yeni Stajyer Bilgileri"));

        gbc.insets = new Insets(5, 5, 5, 5);

        // Ad Soyad
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Ad Soyad:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Başlangıç Tarihi
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Başlangıç Tarihi (yyyy-mm-dd):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        startDateField = new JTextField(20);
        startDateField.setText(LocalDate.now().toString());
        panel.add(startDateField, gbc);

        // Arama Alanı
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Arama (Ad Soyad):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        searchField = new JTextField(20);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterTable();
            }
        });
        panel.add(searchField, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Staj Aşamaları"));

        String[] columns = {
            "Ad Soyad", 
            "Başlangıç Tarihi",
            "Cumhuriyet Başsavcılığı", 
            "Bölge İdare", 
            "Ağır Ceza", 
            "Asliye Ceza", 
            "Sulh Hukuk", 
            "Asliye Hukuk/Asl.Tic/İş", 
            "İcra", 
            "Mazbata"
        };
        tableModel = new DefaultTableModel(columns, 0);
        stageTable = new JTable(tableModel);
        
        // Row sorter ekle (arama için)
        rowSorter = new TableRowSorter<>(tableModel);
        stageTable.setRowSorter(rowSorter);
        
        // Satır seçim ayarları
        stageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stageTable.getSelectionModel().addListSelectionListener(e -> {
            // Seçim değiştiğinde yapılacak işlemler (opsiyonel)
        });
        
        JScrollPane scrollPane = new JScrollPane(stageTable);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Stajyer Ekle");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIntern();
            }
        });

        JButton exportButton = new JButton("CSV'ye Aktar");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCSV();
            }
        });

        JButton deleteButton = new JButton("Seçili Kişiyi Sil");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });

        JButton clearButton = new JButton("Tabloyu Temizle");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                saveData(); // Temizleme sonrası kaydet
            }
        });

        panel.add(addButton);
        panel.add(exportButton);
        panel.add(deleteButton);
        panel.add(clearButton);

        return panel;
    }

    private void addIntern() {
        String name = nameField.getText().trim();
        String startDateStr = startDateField.getText().trim();

        if (name.isEmpty() || startDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            calculateAndAddStages(name, startDate);
            
            // Formu temizle
            nameField.setText("");
            startDateField.setText(LocalDate.now().toString());
            
            JOptionPane.showMessageDialog(this, "Stajyer başarıyla eklendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Geçersiz tarih formatı! (yyyy-mm-dd)", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateAndAddStages(String name, LocalDate startDate) {
        LocalDate currentStartDate = startDate;
        LocalDate lastInternshipEndDate = null;
        
        // Tek satırda tüm bilgileri tutacak array
        Object[] rowData = new Object[10]; // 10 sütun var
        
        rowData[0] = name; // Ad Soyad
        rowData[1] = formatDateWithDayName(startDate); // Başlangıç Tarihi
        
        // Her aşama için başlangıç tarihlerini hesapla
        for (int i = 0; i < stageNames.size(); i++) {
            String stageName = stageNames.get(i);
            int duration = stageDurations.get(i);
            
            if (stageName.equals("Mazbata")) {
                // Mazbata, son staj aşamasından 2 gün sonra verilir
                LocalDate mazbataDate = lastInternshipEndDate.plusDays(2);
                
                // Eğer mazbata tarihi hafta sonuna veya tatile denk gelirse, bir sonraki iş gününe kaydır
                while (isWeekendOrHoliday(mazbataDate)) {
                    mazbataDate = mazbataDate.plusDays(1);
                }
                
                rowData[i + 2] = formatDateWithDayName(mazbataDate);
            } else {
                // Normal aşamalar için başlangıç tarihi
                rowData[i + 2] = formatDateWithDayName(currentStartDate);
                
                LocalDate endDate;
                if (duration == 30) {
                    // 1 aylık aşamalar için özel hesaplama
                    endDate = calculateMonthlyEndDate(currentStartDate);
                } else if (duration == 45) {
                    // 1.5 aylık aşama için özel hesaplama
                    endDate = calculateOneAndHalfMonthEndDate(currentStartDate);
                } else {
                    // Diğer aşamalar için normal hesaplama
                    endDate = calculateEndDate(currentStartDate, duration);
                }
                lastInternshipEndDate = endDate;
                
                // Bir sonraki aşama için başlangıç tarihi
                currentStartDate = endDate.plusDays(1);
                
                // Eğer başlangıç tarihi hafta sonuna veya tatile denk gelirse, bir sonraki iş gününe kaydır
                while (isWeekendOrHoliday(currentStartDate)) {
                    currentStartDate = currentStartDate.plusDays(1);
                }
            }
        }
        
        // Tek satır olarak ekle
        tableModel.addRow(rowData);
        
        // Veriyi kaydet
        saveData();
    }

    private void deleteSelectedRow() {
        int selectedRow = stageTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz kişiyi seçin!", 
                "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Seçili satırdaki kişinin adını al
        String selectedName = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Onay iste
        int confirm = JOptionPane.showConfirmDialog(this, 
            "'" + selectedName + "' adlı kişiyi silmek istediğinizden emin misiniz?", 
            "Silme Onayı", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Seçili satırı sil
            tableModel.removeRow(selectedRow);
            
            // Veriyi kaydet
            saveData();
            
            JOptionPane.showMessageDialog(this, "Kişi başarıyla silindi!", 
                "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void filterTable() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            // Arama boşsa tüm satırları göster
            rowSorter.setRowFilter(null);
        } else {
            // Ad Soyad sütununda (0. indeks) arama yap
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 0));
        }
    }

    private LocalDate calculateEndDate(LocalDate startDate, int duration) {
        // Başlangıç tarihinden itibaren süre kadar gün ekle (hafta sonu dahil)
        LocalDate endDate = startDate.plusDays(duration - 1);
        
        // Eğer son gün hafta sonuna veya tatile denk gelirse, bir sonraki iş gününe kaydır
        while (isWeekendOrHoliday(endDate)) {
            endDate = endDate.plusDays(1);
        }
        
        return endDate;
    }
    
    private LocalDate calculateMonthlyEndDate(LocalDate startDate) {
        // Bir sonraki ayın aynı gününü hesapla (aşama bitiş tarihi)
        // Örnek: 1 Ocak başlangıç → 1 Şubat bitiş
        
        int targetDay = startDate.getDayOfMonth();
        LocalDate nextMonth = startDate.plusMonths(1);
        
        // Hedef ayda bu gün var mı kontrol et
        int daysInTargetMonth = nextMonth.lengthOfMonth();
        
        if (targetDay <= daysInTargetMonth) {
            // Hedef gün mevcut, normal hesaplama
            return nextMonth.withDayOfMonth(targetDay);
        } else {
            // Hedef gün mevcut değil (örneğin 31 Ağustos → Eylül'de 31 yok)
            // Bir sonraki ayın 1'ini kullan (31 Ağustos → 1 Ekim)
            return nextMonth.plusMonths(1).withDayOfMonth(1);
        }
    }
    
    private LocalDate calculateOneAndHalfMonthEndDate(LocalDate startDate) {
        // Önce 1 aylık hesaplama yap (aynı mantıkla)
        int targetDay = startDate.getDayOfMonth();
        LocalDate nextMonth = startDate.plusMonths(1);
        
        // Hedef ayda bu gün var mı kontrol et
        int daysInTargetMonth = nextMonth.lengthOfMonth();
        
        LocalDate oneMonthLater;
        if (targetDay <= daysInTargetMonth) {
            // Hedef gün mevcut, normal hesaplama
            oneMonthLater = nextMonth.withDayOfMonth(targetDay);
        } else {
            // Hedef gün mevcut değil (örneğin 31 Ağustos → Eylül'de 31 yok)
            // Bir sonraki ayın 1'ini kullan (31 Ağustos → 1 Ekim)
            oneMonthLater = nextMonth.plusMonths(1).withDayOfMonth(1);
        }
        
        // Bu tarihe 15 gün daha ekle (aşama bitiş tarihi)
        // Örnek: 1 Ekim + 15 gün = 16 Ekim bitiş
        LocalDate stageEndDate = oneMonthLater.plusDays(15);
        
        // Aşama bitiş tarihini döndür
        // Ana metod bu tarihe +1 gün ekleyerek değişim tarihini (16. gün) hesaplayacak
        return stageEndDate;
    }

    private boolean isWeekendOrHoliday(LocalDate date) {
        // Hafta sonu kontrolü
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true;
        }
        
        // Resmi tatil kontrolü
        return holidays.contains(date);
    }
    
    private String formatDateWithDayName(LocalDate date) {
        // Türkçe gün isimleri
        String[] dayNames = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
        String dayName = dayNames[date.getDayOfWeek().getValue() - 1];
        
        // Tarih formatı: dd.MM.yyyy (Gün Adı)
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " (" + dayName + ")";
    }

    private void exportToCSV() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tabloda veri yok!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("CSV Dosyası Kaydet");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try {
                exportTableToCSV(file);
                JOptionPane.showMessageDialog(this, "CSV dosyası başarıyla kaydedildi:\n" + file.getAbsolutePath(), 
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "CSV dosyası kaydedilirken hata oluştu:\n" + e.getMessage(), 
                    "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportTableToCSV(File file) throws IOException {
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
             java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(fos, StandardCharsets.UTF_8);
             java.io.PrintWriter writer = new java.io.PrintWriter(osw)) {
            
            // UTF-8 BOM ekle (Excel'de Türkçe karakterlerin doğru görünmesi için)
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);
            
            // Başlık satırı
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                if (i > 0) header.append(";"); // Excel için noktalı virgül kullan
                String columnName = tableModel.getColumnName(i);
                // CSV için özel karakterleri escape et
                columnName = columnName.replace("\"", "\"\"");
                header.append("\"").append(columnName).append("\"");
            }
            writer.println(header.toString());
            
            // Veri satırları
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    if (j > 0) row.append(";"); // Excel için noktalı virgül kullan
                    Object value = tableModel.getValueAt(i, j);
                    if (value != null) {
                        // CSV için özel karakterleri escape et
                        String cellValue = value.toString().replace("\"", "\"\"");
                        row.append("\"").append(cellValue).append("\"");
                    } else {
                        row.append("\"\"");
                    }
                }
                writer.println(row.toString());
            }
            
            writer.flush();
        }
    }
    
    // Veri kaydetme metodu
    private void saveData() {
        try {
            StringBuilder data = new StringBuilder();
            
            // Tablo verilerini kaydet
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    Object value = tableModel.getValueAt(i, j);
                    data.append(value != null ? value.toString() : "");
                    if (j < tableModel.getColumnCount() - 1) {
                        data.append("|"); // Sütun ayırıcı
                    }
                }
                data.append("\n"); // Satır sonu
            }
            
            // Dosyaya yaz
            Files.write(Paths.get(DATA_FILE), data.toString().getBytes(StandardCharsets.UTF_8));
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Veri kaydedilirken hata oluştu: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Veri yükleme metodu
    private void loadData() {
        try {
            if (Files.exists(Paths.get(DATA_FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(DATA_FILE), StandardCharsets.UTF_8);
                
                // Mevcut verileri temizle
                tableModel.setRowCount(0);
                
                // Her satırı oku ve tabloya ekle
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        String[] values = line.split("\\|");
                        Object[] rowData = new Object[tableModel.getColumnCount()];
                        
                        for (int i = 0; i < Math.min(values.length, rowData.length); i++) {
                            rowData[i] = values[i].isEmpty() ? "" : values[i];
                        }
                        
                        tableModel.addRow(rowData);
                    }
                }
                
                // Arama filtresini temizle
                if (searchField != null) {
                    searchField.setText("");
                    rowSorter.setRowFilter(null);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Veri yüklenirken hata oluştu: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Varsayılan look and feel kullan
                }
                new Main();
            }
        });
    }
}