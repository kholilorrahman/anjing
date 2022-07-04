package frame;

import helpers.koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnjingInputFram extends JFrame {
    private JPanel mainPanel;
    private JTextField id_anjingTextField;
    private JTextField jenis_anjingTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JTextField warnaTextField;
    private JTextField BeratTextField;
    private JTextField umurTextField;
    private JTextField HargaTextField;
    private JTextField jenis_kelaminTextField;

    private int id_anjing;

    public void setId_anjing(int id_anjing) {
        this.id_anjing = id_anjing;
    }

    public AnjingInputFram() {
        simpanButton.addActionListener(e -> {
            String jenis_anjing = jenis_anjingTextField.getText();
            String warna = warnaTextField.getText();
            String berat = BeratTextField.getText();
            String umur = umurTextField.getText();
            String harga = HargaTextField.getText();
            String jenis_kelamin = jenis_kelaminTextField.getText();
            Connection c = koneksi.getConnection();
            PreparedStatement ps;
            if (jenis_anjing.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data Jenis Anjing",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                jenis_anjingTextField.requestFocus();
                return;
            }
            try {
                String cekSQL;
                if (this.id_anjing == 0) { //jika TAMBAH

                    cekSQL = "SELECT * FROM anjing WHERE jenis_anjing=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, jenis_anjing);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Jenis Anjing sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO anjing (id_anjing,jenis_anjing,warna,berat,umur,harga,jenis_kelamin) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";
                    insertSQL = "INSERT INTO `anjing` (`id_anjing`, `jenis_anjing`, `warna`, `berat`, `umur`, `harga`, `jenis_kelamin`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `anjing` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO anjing (jenis_anjing,warna,berat,umur,harga,jenis_kelamin) VALUES (?)";
                    insertSQL = "INSERT INTO anjing SET jenis_anjing=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, jenis_anjing);
                    ps.setString(2, warna);
                    ps.setString(3, berat);
                    ps.setString(4, umur);
                    ps.setString(5, harga);
                    ps.setString(6, jenis_kelamin);
                } else {
                    cekSQL = "SELECT * FROM anjing WHERE jenis_anjing=? AND warna=? AND berat=? AND umur=? AND harga=? AND jenis_kelamin=? AND id_anjing!=?";
                    ps = c.prepareStatement(cekSQL);
                        ps.setString(1, jenis_anjing);
                        ps.setString(2, warna);
                        ps.setString(3, berat);
                        ps.setString(4, umur);
                        ps.setString(5, harga);
                        ps.setString(6, jenis_kelamin);
                        ps.setInt(7, id_anjing);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                        String updateSQL = "UPDATE anjing SET jenis_anjing=?,warna=?, berat=?, umur=?, harga=?, jenis_kelamin=? WHERE id_anjing=?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, jenis_anjing);
                        ps.setString(2, warna);
                        ps.setString(3, berat);
                        ps.setString(4, umur);
                        ps.setString(5, harga);
                        ps.setString(6, jenis_kelamin);
                        ps.setInt(7, id_anjing);
                }
                ps.executeUpdate();
                dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        batalButton.addActionListener(e -> dispose());
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Input anjing");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen() {
        Connection c = koneksi.getConnection();
        String findSQL = "SELECT * FROM anjing WHERE id_anjing = ?";
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id_anjing);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id_anjingTextField.setText(String.valueOf(rs.getInt("id_anjing")));
                jenis_anjingTextField.setText(rs.getString("jenis_anjing"));
                warnaTextField.setText(rs.getString("warna"));
                BeratTextField.setText(rs.getString("berat"));
                umurTextField.setText(rs.getString("umur"));
                HargaTextField.setText(rs.getString("harga"));
                jenis_kelaminTextField.setText(rs.getString("jenis_kelamin"));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

