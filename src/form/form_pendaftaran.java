/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;
import com.toedter.calendar.JDateChooser;
import config.koneksi;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author user
 */
public class form_pendaftaran extends javax.swing.JPanel {

    /**
     * Creates new form form_pendaftaran
     */
    HashMap<String, String> mapDokter = new HashMap<>();
    HashMap<String, String> mapLayanan = new HashMap<>();
    
    public form_pendaftaran() {
        initComponents();
        isiComboPasien();
        JDateChooser dateChooser = new JDateChooser();
        showDateTime();
//        loadPasien();
        loadDokter();
        loadLayanan();
        loadJenisKelamin();
        generateIdPendaftaran();
        
        //Tgl Lahir
        JDateChooser TanggalLahir = new JDateChooser();
        TanggalLahir.setDateFormatString("yyyy-MM-dd"); // format sesuai DB
        form_pendaftaran.add(TanggalLahir); // masukkan ke panel
        
    //Isi Combo untuk nilai default
    cbPasien.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String selected = cbPasien.getSelectedItem().toString();
            if ("-- ID Baru --".equals(selected)) {
                try {
                    Connection kon = koneksi.koneksiDb();
                    Statement st = kon.createStatement();
                    ResultSet rs = st.executeQuery("SELECT MAX(No_rm) AS max_id FROM pasien");
                    if (rs.next()) {
                        int nextId = rs.getInt("max_id") + 1;
                        cbPasien.setEditable(true);
                        cbPasien.setSelectedItem(String.valueOf(nextId));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                cbPasien.setEditable(false);
            }
        }
    });

     //data pasien
    cbPasien.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selected = cbPasien.getSelectedItem().toString();

                if ("-- ID Baru --".equals(selected)) {
                    // Logika jika Pasien Baru
                    idBaruLogic();
                } else {
                    // Logika jika Pasien Lama (Lakukan Deteksi)
                    cbPasien.setEditable(false);
                    tampilkanDataPasien(selected);
                }
            }
        }
    });
        
    //tambah layanan   
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Layanan", "Harga"}, 0);
        table_layanan.setModel(model);
        
        
        
        
    }
    
    private void isiComboPasien() {
        cbPasien.removeAllItems();
        cbPasien.addItem("-- ID Baru --");
        try {
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();
            ResultSet rs = st.executeQuery("SELECT No_rm FROM pasien ORDER BY No_rm ASC");
            while (rs.next()) {
                cbPasien.addItem(rs.getString("No_rm"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            }   
        }
    
    private void showDateTime() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();
                SimpleDateFormat tanggal = new SimpleDateFormat("yyyy-MM-dd ");
                lblTanggal.setText(tanggal.format(now));
            }
        });
        timer.start();
    }
    
//    private void loadPasien() {
//        try {
//            String sql = "SELECT No_rm FROM pasien";
//            Connection kon = koneksi.koneksiDb();
//            PreparedStatement pst = kon.prepareStatement(sql);
//            ResultSet rs = pst.executeQuery();
//
//            // 3. Hapus data lama dari combobox
//            cbPasien.removeAllItems();
//
//            // 4. Tambahkan data baru dari database
//            while (rs.next()) {
//                String nama = rs.getString("No_rm");
//                cbPasien.addItem(nama);
//            }
//
//            kon.close();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Gagal load data pasien: " + e.getMessage());
//        }
//    }
    private void loadDokter() {
        
        try {
            String sql = "SELECT Id_Dokter, Nama FROM dokter";
            Connection kon = koneksi.koneksiDb();
            PreparedStatement pst = kon.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // 3. Hapus data lama dari combobox
            Dokter.removeAllItems();
            mapDokter.clear();
            
            // 4. Tambahkan data baru dari database
            while (rs.next()) {
                String id = rs.getString("Id_Dokter");
                String nama = rs.getString("Nama");

            Dokter.addItem(nama); // tampilkan nama
            mapDokter.put(nama, id); // simpan relasi nama → id
            }

            kon.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data Dokter: " + e.getMessage());
        }
    }
    private void loadLayanan() {
        try {
            String sql = "SELECT id_layanan, nama_layanan FROM layanan";
            Connection kon = koneksi.koneksiDb();
            PreparedStatement pst = kon.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // 3. Hapus data lama dari combobox
            Layanan.removeAllItems();
            mapLayanan.clear();
            // 4. Tambahkan data baru dari database
            while (rs.next()) {
                String id = rs.getString("id_layanan");
                String nama = rs.getString("nama_layanan");

            Layanan.addItem(nama); // tampilkan nama
            mapLayanan.put(nama, id); // simpan relasi nama → id
            }

            kon.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data Dokter: " + e.getMessage());
        }
    }
    
     private void loadJenisKelamin() {
        try {
            String sql = "SHOW COLUMNS FROM pasien LIKE 'jk'";
            Connection kon = koneksi.koneksiDb();
            PreparedStatement pst = kon.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String enumStr = rs.getString("Type"); 
                enumStr = enumStr.replace("enum(", "").replace(")", "").replace("'", "");
                String[] values = enumStr.split(",");

                cbjk.removeAllItems();
                for (String value : values) {
                    cbjk.addItem(value);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load ENUM: " + e.getMessage());
        }
    }
    
     private void generateIdPendaftaran() {
        try {
            Connection kon = koneksi.koneksiDb();
            LocalDate now = LocalDate.now();
            String tanggalStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String tanggalId = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String sql = "SELECT COUNT(*) as total FROM pendaftaran WHERE tanggal_daftar = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, tanggalStr);
            ResultSet rs = pst.executeQuery();

            int urut = 1;
            if (rs.next()) {
                urut = rs.getInt("total") + 1;
            }
            String noUrutStr = String.format("%03d", urut);
            idPendaftaran.setText(tanggalId + noUrutStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal generate ID: " + e.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popup_layanan = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        main_panel = new javax.swing.JPanel();
        form_pendaftaran = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbPasien = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Dokter = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bt_layanan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_layanan = new javax.swing.JTable();
        SimpanDaftar = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        Layanan = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        txtNama = new javax.swing.JTextField();
        txttelp = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cbjk = new javax.swing.JComboBox<>();
        txtTB = new javax.swing.JTextField();
        txtBB = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        lblTanggal = new javax.swing.JLabel();
        TanggalLahir = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        idPendaftaran = new javax.swing.JFormattedTextField();

        jLabel8.setText("INI TESTER");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel8)
                .addContainerGap(313, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel8)
                .addContainerGap(256, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout popup_layananLayout = new javax.swing.GroupLayout(popup_layanan.getContentPane());
        popup_layanan.getContentPane().setLayout(popup_layananLayout);
        popup_layananLayout.setHorizontalGroup(
            popup_layananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(popup_layananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        popup_layananLayout.setVerticalGroup(
            popup_layananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(popup_layananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setLayout(new java.awt.CardLayout());

        main_panel.setLayout(new java.awt.CardLayout());

        form_pendaftaran.setBackground(new java.awt.Color(255, 255, 255));
        form_pendaftaran.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        form_pendaftaran.setPreferredSize(new java.awt.Dimension(500, 462));

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel1.setText("FORMULIR PENDAFTARAN PERAWATAN");

        jLabel6.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N

        cbPasien.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        cbPasien.setBorder(null);
        cbPasien.setOpaque(false);
        cbPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPasienActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel2.setText("Id Pasien ");

        jLabel3.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel3.setText("Dokter    :");

        Dokter.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        Dokter.setBorder(null);
        Dokter.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel4.setText("Tanggal  :");

        jLabel5.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel5.setText("Layanan :");

        bt_layanan.setBackground(new java.awt.Color(51, 51, 255));
        bt_layanan.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        bt_layanan.setForeground(new java.awt.Color(255, 255, 255));
        bt_layanan.setText("Tambah");
        bt_layanan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bt_layanan.setBorderPainted(false);
        bt_layanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_layananMouseClicked(evt);
            }
        });
        bt_layanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_layananActionPerformed(evt);
            }
        });

        table_layanan.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table_layanan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Layanan", "Harga"
            }
        ));
        table_layanan.setRowHeight(20);
        jScrollPane1.setViewportView(table_layanan);

        SimpanDaftar.setBackground(new java.awt.Color(0, 255, 0));
        SimpanDaftar.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        SimpanDaftar.setText("Simpan");
        SimpanDaftar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SimpanDaftar.setBorderPainted(false);
        SimpanDaftar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanDaftarActionPerformed(evt);
            }
        });

        btn_batal.setBackground(new java.awt.Color(255, 255, 0));
        btn_batal.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        btn_batal.setText("Batal");
        btn_batal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_batal.setBorderPainted(false);
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        Layanan.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        Layanan.setBorder(null);
        Layanan.setOpaque(false);
        Layanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayananActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel7.setText("Nama  ");

        jLabel9.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel9.setText("Alamat ");

        jLabel10.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel10.setText("Jenis Kelamin  ");

        jLabel12.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel12.setText("Tinggi Badan");

        jLabel13.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel13.setText("Tanggal Lahir");

        jLabel14.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel14.setText("Berat Badan");

        jLabel15.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel15.setText(":");

        jLabel17.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel17.setText(":");

        jLabel18.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel18.setText(":");

        jLabel19.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel19.setText(":");

        txtAlamat.setColumns(20);
        txtAlamat.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        txtNama.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtNama.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        txttelp.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txttelp.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel16.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel16.setText("No Telepon");

        jLabel20.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel20.setText(":");

        jLabel21.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel21.setText(":");

        jLabel22.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel22.setText(":");

        jLabel23.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel23.setText(":");

        cbjk.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        cbjk.setBorder(null);
        cbjk.setOpaque(false);

        txtTB.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtTB.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtTB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTBActionPerformed(evt);
            }
        });

        txtBB.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtBB.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtBB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBBActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        lblTanggal.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        lblTanggal.setText("jLabel11");
        lblTanggal.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        TanggalLahir.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel11.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel11.setText("Nomor    :");

        idPendaftaran.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        idPendaftaran.setFont(new java.awt.Font("Georgia", 2, 18)); // NOI18N

        javax.swing.GroupLayout form_pendaftaranLayout = new javax.swing.GroupLayout(form_pendaftaran);
        form_pendaftaran.setLayout(form_pendaftaranLayout);
        form_pendaftaranLayout.setHorizontalGroup(
            form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, form_pendaftaranLayout.createSequentialGroup()
                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, form_pendaftaranLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(idPendaftaran, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                        .addComponent(Layanan, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(bt_layanan, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Dokter, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                        .addGap(412, 412, 412)
                                        .addComponent(jLabel6))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, form_pendaftaranLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNama))
                                    .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbPasien, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(form_pendaftaranLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, form_pendaftaranLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel20)
                            .addComponent(jLabel15)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbjk, javax.swing.GroupLayout.Alignment.CENTER, 0, 162, Short.MAX_VALUE)
                            .addComponent(txtBB, javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txttelp, javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtTB)
                            .addComponent(TanggalLahir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(form_pendaftaranLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SimpanDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(170, 170, 170)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        form_pendaftaranLayout.setVerticalGroup(
            form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(form_pendaftaranLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel21)
                    .addComponent(cbjk, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtTB, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, form_pendaftaranLayout.createSequentialGroup()
                            .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17))
                            .addGap(18, 18, 18))
                        .addGroup(form_pendaftaranLayout.createSequentialGroup()
                            .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel23)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBB, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(TanggalLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(txttelp, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(form_pendaftaranLayout.createSequentialGroup()
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(idPendaftaran, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel3)
                            .addComponent(Dokter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4)
                            .addComponent(lblTanggal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel5)
                            .addComponent(Layanan, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_layanan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(form_pendaftaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SimpanDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(94, 94, 94)
                        .addComponent(jLabel6))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(154, 154, 154))
        );

        main_panel.add(form_pendaftaran, "card2");

        add(main_panel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void LayananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayananActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LayananActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        // TODO add your handling code here:
        resetdaftar();
    }//GEN-LAST:event_btn_batalActionPerformed

    private void SimpanDaftarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanDaftarActionPerformed
        // TODO add your handling code here:
  try {
    Connection kon = koneksi.koneksiDb();
    kon.setAutoCommit(false);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String tglLahir = sdf.format(TanggalLahir.getDate());

    // Ambil data dari form
    String id_pendaftaran = idPendaftaran.getText();  // ID pendaftaran sudah digenerate manual
    String No_rm;
    String nama = txtNama.getText();
    String alamat = txtAlamat.getText();
    String jk = cbjk.getSelectedItem().toString();
    String tinggi = txtTB.getText();
    String berat = txtBB.getText();
    String telp = txttelp.getText();
    String tanggalInput = lblTanggal.getText();
    String namaDokterDipilih = Dokter.getSelectedItem().toString();
    String idDokter = mapDokter.get(namaDokterDipilih);

    // === CEK APAKAH PASIEN BARU ===
    if (cbPasien.getSelectedItem().toString().equals("-- ID Baru --")) {
        // Simpan pasien baru
        PreparedStatement psPasien = kon.prepareStatement(
            "INSERT INTO pasien (Nama, Alamat, jk, Tinggi, Berat, Tgl_lahir, no_telp) VALUES (?, ?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        psPasien.setString(1, nama);
        psPasien.setString(2, alamat);
        psPasien.setString(3, jk);
        psPasien.setString(4, tinggi);
        psPasien.setString(5, berat);
        psPasien.setString(6, tglLahir);
        psPasien.setString(7, telp);
        psPasien.executeUpdate();

        ResultSet rs = psPasien.getGeneratedKeys();
        if (rs.next()) {
            No_rm = String.valueOf(rs.getInt(1)); // Ambil No_rm dari DB
        } else {
            throw new SQLException("Gagal menyimpan pasien baru, ID tidak tersedia.");
        }

        rs.close();
        psPasien.close();
    } else {
        No_rm = cbPasien.getSelectedItem().toString();
    }

    // === SIMPAN PENDAFTARAN ===
    PreparedStatement psPendaftaran = kon.prepareStatement(
        "INSERT INTO pendaftaran (id_pendaftaran, No_rm, Id_dokter, tanggal_daftar) VALUES (?, ?, ?, ?)"
    );
    psPendaftaran.setString(1, id_pendaftaran); // manual ID
    psPendaftaran.setString(2, No_rm);
    psPendaftaran.setString(3, idDokter);
    psPendaftaran.setString(4, tanggalInput);
    psPendaftaran.executeUpdate();

    // === SIMPAN DETAIL LAYANAN ===
    DefaultTableModel model = (DefaultTableModel) table_layanan.getModel();
    PreparedStatement psDetail = kon.prepareStatement(
        "INSERT INTO pendaftaran_detail (id_pendaftaran, id_layanan, harga) VALUES (?, ?, ?)"
    );

    for (int i = 0; i < model.getRowCount(); i++) {
        int harga = Integer.parseInt(model.getValueAt(i, 1).toString());
        String namaLayananDipilih = model.getValueAt(i, 0).toString();
        String idLayanan = mapLayanan.get(namaLayananDipilih);

        psDetail.setString(1, id_pendaftaran);  // pakai id_pendaftaran dari awal
        psDetail.setString(2, idLayanan);
        psDetail.setInt(3, harga);
        psDetail.addBatch();
    }

    psDetail.executeBatch();
    kon.commit(); // SELESAIKAN TRANSAKSI
    

    //JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
    try {
//        File file = new File("src/laporan/nota_pendaftaran.jasper"); // path ke file .jasper
        Map<String, Object> param = new HashMap<>();
         param.put("id_pendaftaran",idPendaftaran.getText()); // ambil dari input user atau combo box

        JasperReport report = JasperCompileManager.compileReport("src/report/nota_pendaftaran.jrxml");
        JasperPrint cetak = JasperFillManager.fillReport(report, param, kon);
        JasperViewer.viewReport(cetak, false);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error mencetak: " + e.getMessage());
    }
    resetdaftar(); // reset form

} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
}

    }//GEN-LAST:event_SimpanDaftarActionPerformed

    private void bt_layananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_layananActionPerformed
        // TODO add your handling code here:
        // 1. Ambil data dari JComboBox Layanan
        String namaLayanan = Layanan.getSelectedItem().toString();

        // Validasi agar tidak menambah pilihan default/kosong
        if (namaLayanan.equals("Pilih Layanan") || namaLayanan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih layanan terlebih dahulu!");
            return;
        }

        // 2. Ambil harga menggunakan method yang sudah Anda buat
        int harga = ambilHargaLayanan(namaLayanan); 

        // 3. Masukkan ke Tabel Preview
        DefaultTableModel model = (DefaultTableModel) table_layanan.getModel();

        // Cek apakah layanan sudah ada di tabel (mencegah duplikat)
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(namaLayanan)) {
                JOptionPane.showMessageDialog(this, "Layanan ini sudah ditambahkan!");
                return;
            }
        }

        // Tambahkan baris baru [Nama Layanan, Harga]
        model.addRow(new Object[]{namaLayanan, harga});
    }//GEN-LAST:event_bt_layananActionPerformed

    private void bt_layananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_layananMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_bt_layananMouseClicked

    private void cbPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPasienActionPerformed

    private void txtTBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTBActionPerformed

    private void txtBBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Dokter;
    private javax.swing.JComboBox<String> Layanan;
    private javax.swing.JButton SimpanDaftar;
    private com.toedter.calendar.JDateChooser TanggalLahir;
    private javax.swing.JButton bt_layanan;
    private javax.swing.JButton btn_batal;
    private javax.swing.JComboBox<String> cbPasien;
    private javax.swing.JComboBox<String> cbjk;
    private javax.swing.JPanel form_pendaftaran;
    private javax.swing.JFormattedTextField idPendaftaran;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JPanel main_panel;
    private javax.swing.JDialog popup_layanan;
    private javax.swing.JTable table_layanan;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtBB;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtTB;
    private javax.swing.JTextField txttelp;
    // End of variables declaration//GEN-END:variables

    private void idBaruLogic() {
        try {
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(No_rm) AS max_id FROM pasien");

            int nextId = 1;
            if (rs.next()) {
                nextId = rs.getInt("max_id") + 1;
            }

            // Reset semua field agar kosong untuk input baru
            txtNama.setText("");
            txtAlamat.setText("");
            cbjk.setSelectedIndex(0);
            txtTB.setText("");
            txtBB.setText("");
            TanggalLahir.setDate(null);
            txttelp.setText("");

            // Aktifkan mode edit untuk menampilkan ID baru
            cbPasien.setEditable(true);
            cbPasien.setSelectedItem(String.valueOf(nextId));
            txtNama.requestFocus(); // Langsung fokus ke Nama

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
        private void tampilkanDataPasien(String idPasien) { 
        // Jika string kosong atau ID Baru, jangan jalankan query
        if (idPasien == null || idPasien.isEmpty() || idPasien.equals("-- ID Baru --")) {
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Connection kon = koneksi.koneksiDb();
            String sql = "SELECT * FROM pasien WHERE No_rm = ?";
            PreparedStatement ps = kon.prepareStatement(sql);
            ps.setString(1, idPasien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtNama.setText(rs.getString("Nama"));
                txtAlamat.setText(rs.getString("Alamat"));
                cbjk.setSelectedItem(rs.getString("jk"));
                txtTB.setText(rs.getString("Tinggi"));
                txtBB.setText(rs.getString("Berat"));

                String tglStr = rs.getString("tgl_lahir");
                if (tglStr != null && !tglStr.isEmpty()) {
                    TanggalLahir.setDate(sdf.parse(tglStr));
                }
                txttelp.setText(rs.getString("no_telp"));
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            // Abaikan error jika data memang belum ada (saat mengetik ID baru)
        }
    }
    
    private int ambilHargaLayanan(String namaLayanan) {
    int harga = 0;
    try {
        Connection kon = koneksi.koneksiDb();
        String sql = "SELECT harga FROM layanan WHERE nama_layanan = ?";
        PreparedStatement ps = kon.prepareStatement(sql);
        ps.setString(1, namaLayanan);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            harga = rs.getInt("harga");
        }

        rs.close();
        ps.close();
        kon.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal ambil harga: " + e.getMessage());
    }
    return harga;
}
    
    private void resetdaftar() {
    // 1. Reset Komponen Pasien
    cbPasien.setSelectedIndex(0); 
    txtNama.setText("");
    txtAlamat.setText("");
    cbjk.setSelectedIndex(0); 
    txtTB.setText("");
    txtBB.setText("");
    TanggalLahir.setDate(null);
    txttelp.setText("");

    // 2. Reset Komponen Pendaftaran
    Dokter.setSelectedIndex(0); 
    Layanan.setSelectedIndex(0);
    
    // Mengatur ulang label tanggal ke hari ini
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    lblTanggal.setText(sdf.format(new Date()));

    // 3. Menghapus semua baris di tabel layanan preview
    DefaultTableModel model = (DefaultTableModel) table_layanan.getModel();
    model.setRowCount(0);
    
    // 4. PENTING: Generate ulang ID Pendaftaran agar sinkron dengan database terbaru
    generateIdPendaftaran();
    
    // 5. Kembalikan fokus ke combobox pasien
    cbPasien.requestFocus();
}
    
}
