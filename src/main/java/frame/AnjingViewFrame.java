package frame;

import helpers.koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class AnjingViewFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField textField1;
    private JButton cariButton;
    private JLabel cariTextField;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public AnjingViewFrame() {
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data dulu"
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            int id_anjing = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            AnjingInputFram inputFram = new AnjingInputFram();
            inputFram.setId_anjing(id_anjing);
            inputFram.isiKomponen();
            inputFram.setVisible(true);
        });
        tambahButton.addActionListener(e -> {
            AnjingInputFram inputFram = new AnjingInputFram();
            inputFram.setVisible(true);
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null, "Pilih data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin mau hapus?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );

            if (pilihan == 0) {
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                Connection c = koneksi.getConnection();
                String deleteSQL = "DELETE FROM anjing WHERE id_anjing = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cariButton.addActionListener(e -> {

            if (cariTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Kata Kunci Pencarian",
                        "Validasi Kata Kunci kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Connection c = koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM anjing WHERE jenis_anjing like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                viewTable.setModel(dtm);
                Object[] row = new Object[7];
                while (rs.next()) {
                    row[0] = rs.getInt("id_anjing");
                    row[1] = rs.getString("jenis_anjing");
                    row[2] = rs.getString("warna");
                    row[3] = rs.getString("berat");
                    row[4] = rs.getString("umur");
                    row[5] = rs.getString("harga");
                    row[6] = rs.getString("jenis_kelamin");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        tutupButton.addActionListener(e -> dispose());
        batalButton.addActionListener(e -> isiTable());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                isiTable();
            }
        });
        isiTable();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Data anjing");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable() {
        Connection c = koneksi.getConnection();
        String selectSQL = "SELECT * FROM anjing";

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"id_anjing", "jenis_anjing", "warna", "berat", "umur", "harga", "jenis_kelamin"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            viewTable.getColumnModel().getColumn(1).setMaxWidth(150);
            viewTable.getColumnModel().getColumn(2).setMaxWidth(150);
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            viewTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
            viewTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
            Object[] row = new Object[7];
            while (rs.next()) {
                row[0] = rs.getInt("id_anjing");
                row[1] = rs.getString("jenis_anjing");
                row[2] = rs.getString("warna");
                row[3] = rs.getString("berat");
                row[4] = rs.getString("umur");
                row[5] = rs.getString("harga");
                row[6] = rs.getString("jenis_kelamin");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
