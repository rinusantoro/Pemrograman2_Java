package MySQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DataMHSGUI extends JFrame {

    // Komponen GUI
    JTextField txtNim, txtNama, txtSemester, txtKelas;
    JButton btnInsert, btnUpdate, btnDelete, btnRefresh;
    JTable table;
    DefaultTableModel model;

    // Variabel Database
    Connection con;
    Statement stm;
    ResultSet rs;

    public DataMHSGUI() {
        setTitle("Sistem Data Mahasiswa");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Panel Input ---
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

        btnInsert = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        panelInput.add(btnInsert);
        panelInput.add(btnUpdate);

        add(panelInput, BorderLayout.NORTH);

        // --- Tabel Data ---
        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Semester", "Kelas"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Panel Tombol Bawah ---
        JPanel panelBawah = new JPanel();
        btnDelete = new JButton("Hapus");
        btnRefresh = new JButton("Refresh Tabel");
        panelBawah.add(btnDelete);
        panelBawah.add(btnRefresh);
        add(panelBawah, BorderLayout.SOUTH);

        // Koneksi Database & Load Data
        koneksi();
        loadData();

        // --- Event Handlers ---
        
        // Tombol Tambah
        btnInsert.addActionListener(e -> {
            try {
                String sql = "INSERT INTO datamhs VALUES ('" + txtNim.getText() + "', '" 
                             + txtNama.getText() + "', '" + txtSemester.getText() + "', '" 
                             + txtKelas.getText() + "')";
                stm.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                loadData();
                bersih();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Gagal Simpan: " + ex.getMessage());
            }
        });

        // Tombol Update (Berdasarkan NIM)
        btnUpdate.addActionListener(e -> {
            try {
                String sql = "UPDATE datamhs SET nama='" + txtNama.getText() 
                             + "', semester='" + txtSemester.getText() 
                             + "', kelas='" + txtKelas.getText() 
                             + "' WHERE nim='" + txtNim.getText() + "'";
                stm.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate");
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Gagal Update: " + ex.getMessage());
            }
        });

        // Tombol Hapus (Klik baris di tabel dulu atau isi NIM)
        btnDelete.addActionListener(e -> {
            try {
                String sql = "DELETE FROM datamhs WHERE nim='" + txtNim.getText() + "'";
                stm.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                loadData();
                bersih();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Gagal Hapus: " + ex.getMessage());
            }
        });

        // Refresh
        btnRefresh.addActionListener(e -> loadData());
        
        // Klik Tabel untuk ambil data ke Form
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
            con = DriverManager.getConnection("jdbc:mysql://localhost/mhs", "root", "");
            stm = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + e.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0); // Kosongkan tabel
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bersih() {
        txtNim.setText("");
        txtNama.setText("");
        txtSemester.setText("");
        txtKelas.setText("");
    }

    public static void main(String[] args) {
        new DataMHSGUI().setVisible(true);
    }
}