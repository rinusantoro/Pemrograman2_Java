package MySQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


// Import untuk JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author Lenovo
 */
public class ViewDataGUI extends JFrame {

    // Komponen UI
    private JTextField txtNim, txtNama, txtSemester, txtKelas;
    private JButton btnInsert, btnUpdate, btnDelete, btnPrint, btnRefresh;
    private JTable table;
    private DefaultTableModel model;

    // Variabel Database
    private Connection con;
    private Statement stm;
    private ResultSet rs;

    public ViewDataGUI() {
        // Pengaturan Frame Utama
        setTitle("Aplikasi Data Mahasiswa - CRUD & Report");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. PANEL INPUT (Bagian Atas) ---
        JPanel panelInput = new JPanel(new GridLayout(5, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelInput.add(new JLabel("NIM:"));
        txtNim = new JTextField();
        panelInput.add(txtNim);

        panelInput.add(new JLabel("Nama:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);

        panelInput.add(new JLabel("Semester:"));
        txtSemester = new JTextField();
        panelInput.add(txtSemester);

        panelInput.add(new JLabel("Kelas:"));
        txtKelas = new JTextField();
        panelInput.add(txtKelas);

        btnInsert = new JButton("Tambah Data");
        btnUpdate = new JButton("Update Data");
        panelInput.add(btnInsert);
        panelInput.add(btnUpdate);

        add(panelInput, BorderLayout.NORTH);

        // --- 2. TABEL DATA (Bagian Tengah) ---
        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Semester", "Kelas"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- 3. PANEL TOMBOL AKSI (Bagian Bawah) ---
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnDelete = new JButton("Hapus");
        btnPrint = new JButton("Cetak Laporan");
        btnRefresh = new JButton("Refresh");

        // Memberikan warna khusus pada tombol cetak
        btnPrint.setBackground(new Color(0, 153, 76));
        btnPrint.setForeground(Color.WHITE);

        panelAksi.add(btnDelete);
        panelAksi.add(btnRefresh);
        panelAksi.add(btnPrint);
        add(panelAksi, BorderLayout.SOUTH);

        // Inisialisasi Database
        koneksi();
        loadData();

        // --- EVENT HANDLERS ---

        // Tombol Tambah
        btnInsert.addActionListener(e -> {
            try {
                String sql = "INSERT INTO datamhs VALUES ('" + txtNim.getText() + "', '" 
                             + txtNama.getText() + "', '" + txtSemester.getText() + "', '" 
                             + txtKelas.getText() + "')";
                stm.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Berhasil Menambah Data!");
                loadData();
                bersih();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Gagal Tambah: " + ex.getMessage());
            }
        });

        // Tombol Update
        btnUpdate.addActionListener(e -> {
            try {
                String sql = "UPDATE datamhs SET nama='" + txtNama.getText() 
                             + "', semester='" + txtSemester.getText() 
                             + "', kelas='" + txtKelas.getText() 
                             + "' WHERE nim='" + txtNim.getText() + "'";
                int res = stm.executeUpdate(sql);
                if (res > 0) {
                    JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate!");
                    loadData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Gagal Update: " + ex.getMessage());
            }
        });

        // Tombol Hapus
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM datamhs WHERE nim='" + txtNim.getText() + "'";
                    stm.executeUpdate(sql);
                    loadData();
                    bersih();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Gagal Hapus: " + ex.getMessage());
                }
            }
        });

        // Tombol Refresh
        btnRefresh.addActionListener(e -> loadData());

        // --- TOMBOL CETAK REPORT ---
        btnPrint.addActionListener(e -> {
            File dir1   = new File(".");
            String dirr = dir1.getAbsolutePath();
            String reportPath = dirr + File.separator + "src"
                + File.separator + "MySQL"
                + File.separator + "LaporanMahasiswa.jrxml";
        
            try {
            // Compile .jrxml menjadi .jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // Parmeter
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("judul", "Daftar Data Mahasiswa");

            // Fill isi report nya
            JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperReport, parameters, con
            );

            // View - JasperViewer
            JasperViewer.viewReport(jasperPrint, false);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                "Gagal memproses laporan!\n" + ex.getMessage(),
                "Error JasperReports", JOptionPane.ERROR_MESSAGE);       
            }
        });

        // Event Klik Tabel: Pindahkan data dari tabel ke kotak input
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtNim.setText(model.getValueAt(row, 0).toString());
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtSemester.setText(model.getValueAt(row, 2).toString());
                txtKelas.setText(model.getValueAt(row, 3).toString());
            }
        });
    }

    private void koneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/mhs";
            String user = "root";
            String pass = "";
            con = DriverManager.getConnection(url, user, pass);
            stm = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi Database Gagal: " + e.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0); 
        try {
            rs = stm.executeQuery("SELECT * FROM datamhs");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("semester"),
                    rs.getString("kelas")
                });
            }
        } catch (SQLException e) {
            System.err.println("Gagal Load Data: " + e.getMessage());
        }
    }

    private void bersih() {
        txtNim.setText("");
        txtNama.setText("");
        txtSemester.setText("");
        txtKelas.setText("");
        txtNim.requestFocus();
    }

    public static void main(String[] args) {
        // Menjalankan UI dalam thread khusus (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            new ViewDataGUI().setVisible(true);
        });
    }
}