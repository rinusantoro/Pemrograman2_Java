/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Lenovo
 */
public class FormMahasiswa extends JFrame {
    private JTextField txtNim, txtNama, txtNilai;
    private JButton btnTabel;
    private JTable tabel;
    private DefaultTableModel model;

    public FormMahasiswa() {
        setTitle("Form Mahasiswa");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Label
        JLabel lblNim = new JLabel("NIM");
        lblNim.setBounds(30, 30, 120, 25);
        add(lblNim);

        JLabel lblNama = new JLabel("Nama Mahasiswa");
        lblNama.setBounds(30, 70, 120, 25);
        add(lblNama);

        JLabel lblNilai = new JLabel("Nilai");
        lblNilai.setBounds(30, 110, 120, 25);
        add(lblNilai);

        // TextField
        txtNim = new JTextField();
        txtNim.setBounds(160, 30, 200, 25);
        add(txtNim);

        txtNama = new JTextField();
        txtNama.setBounds(160, 70, 200, 25);
        add(txtNama);

        txtNilai = new JTextField();
        txtNilai.setBounds(160, 110, 200, 25);
        add(txtNilai);

        // Button
        btnTabel = new JButton("TABEL");
        btnTabel.setBounds(380, 30, 100, 25);
        add(btnTabel);

        // Table
        model = new DefaultTableModel();
        model.addColumn("NIM");
        model.addColumn("Nama");
        model.addColumn("Nilai");
        model.addColumn("Keterangan");

        tabel = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabel);
        scrollPane.setBounds(30, 160, 520, 150);
        add(scrollPane);

        // Event Button
        btnTabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahData();
            }
        });
    }

    private void tambahData() {
        String nim = txtNim.getText();
        String nama = txtNama.getText();
        String nilaiStr = txtNilai.getText();

        if (nim.isEmpty() || nama.isEmpty() || nilaiStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        int nilai = Integer.parseInt(nilaiStr);
        String keterangan = (nilai >= 75) ? "Lulus" : "Tidak Lulus";

        model.addRow(new Object[]{nim, nama, nilai, keterangan});

        // Clear input
        txtNim.setText("");
        txtNama.setText("");
        txtNilai.setText("");
    }

    public static void main(String[] args) {
        new FormMahasiswa().setVisible(true);
    }
    
}
