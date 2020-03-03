package telas;

import dao.DaoContasMensais;
import dao.DaoEnderecoPessoa;
import entidades.Conta;
import entidades.Enderecopessoa;
import entidades.Geracaocontas;
import entidades.Hidrometro;
import entidades.Taxa;
import entidades.Taxasconta;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import telas.relatorios.TelaRelatoriosContas;

public class GContas extends javax.swing.JFrame {

    private List<Geracaocontas> socios;
    private Map<String, List<Taxa>> taxas;
    private JFrame telaPrincipal;

    public GContas() {
        taxas = new HashMap<>();
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        carregarSocios();
    }

    public GContas(JFrame telaPrincipal) {
        this();
        this.telaPrincipal = telaPrincipal;
        this.telaPrincipal.setEnabled(false);
        this.telaPrincipal.setVisible(false);
        toFront();
    }
  
    private void carregarSocios() {
        socios = new DaoEnderecoPessoa().TodosOsSocios();
    }

    private void preencherTabela() {
        //Se tiver sido selecionado a conta fixa, a tabela será preenchida com as colunas específicas.
        if (jRadioButtonContaFixa.isSelected()) {
            //Table Renderer resposável por centralizar o conteúdo das colunas.
            DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
            centralizar.setHorizontalAlignment(SwingConstants.CENTER);

            //Define o modelo da tabela
            jTable1.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"id", "dataVencimento", "Sócio", "Numero Socio", "CPF", "Numero Endereço", "Valor", "Gerar", "Taxa"}) {
                Class[] types = new Class[]{Integer.class, String.class, String.class, Integer.class, String.class, String.class,
                    BigDecimal.class, Boolean.class, JButton.class};
                
                @Override
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                boolean[] canEdit = new boolean[]{false, false, false, false, false, false, true, true, true};

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {

                    return canEdit[columnIndex];
                }
            });

            //Seta o tipo de Cell Renderer. Aqui eu passei o renderer que criei lá em cima para centralizar o conteúdo.
            jTable1.getColumn("id").setCellRenderer(centralizar);
            jTable1.getColumn("dataVencimento").setCellRenderer(centralizar);
            jTable1.getColumn("Sócio").setCellRenderer(centralizar);
            jTable1.getColumn("Numero Socio").setCellRenderer(centralizar);
            jTable1.getColumn("CPF").setCellRenderer(centralizar);
            jTable1.getColumn("Numero Endereço").setCellRenderer(centralizar);
            jTable1.getColumn("Valor").setCellRenderer(centralizar);
 
        } 

        else if (jRadioButtonContaHidrometro.isSelected()) {

            DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
            centralizar.setHorizontalAlignment(SwingConstants.CENTER);
        
            jTable1.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        
                        "id", 
                        "dataVencimento", 
                        "Sócio", 
                        
                        "Numero Socio", 
                        "CPF", 
                        "Numero Endereço", 
                        
                        "Valor", 
                        "Gerar", 
                        "Taxa", 
                        
                        "medida anterior",
                        "medida atual"//10
                            
                    }) {
                            
                Class[] types = new Class[]{
                    
                        Integer.class, 
                        String.class, 
                        String.class, 
                    
                        Integer.class, 
                        String.class, 
                        String.class,
                        
                        BigDecimal.class, 
                        Boolean.class, 
                        JButton.class, 
                        
                        Double.class, 
                        Double.class//10
                    };

                @Override
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                
                boolean[] canEdit = new boolean[]{
                    
                    false,
                    false,
                    false,
                    
                    false, 
                    false,
                    false,
                    
                    true, 
                    true,
                    true, 
                    
                    true, 
                    true//10
                };

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
           
            jTable1.getColumn("id").setCellRenderer(centralizar);
            jTable1.getColumn("dataVencimento").setCellRenderer(centralizar);
            jTable1.getColumn("Sócio").setCellRenderer(centralizar);
            jTable1.getColumn("Numero Socio").setCellRenderer(centralizar);
            jTable1.getColumn("CPF").setCellRenderer(centralizar);
            jTable1.getColumn("Numero Endereço").setCellRenderer(centralizar);
            jTable1.getColumn("Valor").setCellRenderer(centralizar);
            
            //jTable1.getColumn("Medida anterior").setCellRenderer(centralizar);
            //jTable1.getColumn("Medida atual").setCellRenderer(centralizar);
        }

        ButtonColumn botao = new ButtonColumn(jTable1, 8);
        preencherColunas();

    }
    
 

    private void preencherColunas() {
         if(jDateVencimento.getCalendar() == null){
               JOptionPane.showMessageDialog(this,"Favor Selecione a Data","Erro",JOptionPane.ERROR_MESSAGE); 
               jRadioButtonContaHidrometro.setSelected(false);
               jRadioButtonContaFixa.setSelected(false);
         } else{
        
            
        //Calendário que irá definir a data de vencimento da conta...Deixei fixo um valor de 5 dias após a data atual, pretendo deixar esse valor dinâmico para o usuário.
        Calendar c = jDateVencimento.getCalendar();
      
        try{
            
            if (jRadioButtonContaFixa.isSelected()) {

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                if (!socios.isEmpty()) {

                    for (Geracaocontas s1 : socios) {

                        List<Date> datas = new DaoContasMensais().datasContas(s1.getNumero());

                        if (datas.isEmpty()) {
                            Pattern p = Pattern.compile("Hidrômetro");
                            Matcher m = p.matcher(s1.getCategoria());
                            if (!m.find()) {
                                model.addRow(new Object[]{
                                    s1.getId(), 
                                    SimpleDateFormat.getDateInstance().format(c.getTime()), 
                                    s1.getNome() + " " + s1.getSobrenome(),
                                    s1.getNumerosocio(), 

                                    s1.getCpf(), 
                                    s1.getNumero(), 
                                    s1.getValor(),
                                    false
                                    });
                                }
                        } else {

                            boolean existeContaNoMes = false;
                            for (Date data : datas) {

                                Calendar c1 = Calendar.getInstance();
                                c1.setTime(data);
                                Calendar c2 = (jDateVencimento.getCalendar());

                                if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                                    existeContaNoMes = true;
                                }
                            }
                            if (!existeContaNoMes) {

                                Pattern p = Pattern.compile("Hidrômetro");
                                Matcher m = p.matcher(s1.getCategoria());
                            if (!m.find()) {

                             
                                
                                model.addRow(new Object[]{
                                    s1.getId(), 
                                    SimpleDateFormat.getDateInstance().format(c.getTime()),
                                    s1.getNome() + " " + s1.getSobrenome(),
                                    s1.getNumerosocio(), 
                                    s1.getCpf(), 
                                    s1.getNumero(), 
                                    s1.getValor(),
                                    false
                                    });

                                }
                            }
                        }
                    }
                }
            } 
        
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Selecione a Data","Aviso",JOptionPane.ERROR_MESSAGE);
        }        
        
        try{
                if (jRadioButtonContaHidrometro.isSelected()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    model.setRowCount(0);
                    if (!socios.isEmpty()) {
                        for (Geracaocontas s1 : socios) {

                            List<Date> datas = new DaoContasMensais().datasContas(s1.getNumero());
                            if (datas.isEmpty()) {
                                Pattern p = Pattern.compile("Hidrômetro");
                                Matcher m = p.matcher(s1.getCategoria());
                                if (m.find()) {

                                    model.addRow(new Object[]{
                                        s1.getId(), 
                                        SimpleDateFormat.getDateInstance().format(c.getTime()),
                                        s1.getNome() + " " + s1.getSobrenome(),
                                        s1.getNumerosocio(),
                                        s1.getCpf(), 
                                        s1.getNumero(), 
                                        s1.getValor(), 
                                        false,
                                        false    
                                        });

                                }
                            } else {
                                boolean existeContaNoMes = false;

                                for (Date data : datas) {

                                    Calendar c1 = Calendar.getInstance();
                                    c1.setTime(data);
                                    Calendar c2 = (jDateVencimento.getCalendar());
                                    if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                                        existeContaNoMes = true;
                                        break;

                                    }

                                }

                                if (!existeContaNoMes) {
                                Pattern p = Pattern.compile("Hidrômetro");
                                Matcher m = p.matcher(s1.getCategoria());
                                if (m.find()) {

                                        model.addRow(new Object[]{
                                            s1.getId(), 
                                            SimpleDateFormat.getDateInstance().format(c.getTime()),
                                            s1.getNome() + " " + s1.getSobrenome(),
                                            s1.getNumerosocio(),
                                            s1.getCpf(),
                                            s1.getNumero(),
                                            s1.getValor(),
                                            false,
                                            false
                                        });

                                      }
                                }


                            }
                        }
                    }

                } 
            
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,"Selecione a Data","Aviso",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chamarTaxas() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setValueAt(BigDecimal.valueOf(0), jTable1.getSelectedRow(), 6);

    }

    //Classe que adiciona o botão de adicionar taxas na tabela.
    private class ButtonColumn extends AbstractCellEditor  implements TableCellRenderer, TableCellEditor, ActionListener {

        JTable table;
        JButton renderButton;
        JButton editButton;
        String text;

        public ButtonColumn(JTable table, int column) {
            super();
            this.table = table;
            renderButton = new JButton();

            editButton = new JButton();
            editButton.setFocusPainted(false);
            editButton.addActionListener(this);

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setCellRenderer(this);
            columnModel.getColumn(column).setCellEditor(this);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (hasFocus) {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            } else if (isSelected) {
                renderButton.setForeground(table.getSelectionForeground());
                renderButton.setBackground(table.getSelectionBackground());
            } else {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }

            renderButton.setText((value == null) ? "Adicionar" : value.toString());
            return renderButton;
        }

        @Override
        public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int column) {
            text = (value == null) ? "Adicionar" : value.toString();
            editButton.setText(text);
            return editButton;
        }

        @Override
        public Object getCellEditorValue() {
            return text;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();

            chamarTaxas();

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jRadioButtonContaFixa = new javax.swing.JRadioButton();
        jRadioButtonContaHidrometro = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jDateVencimento = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jDateReferencia = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/img/ico.png")).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        jTable1.setSelectionBackground(new java.awt.Color(51, 255, 255));
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        buttonGroup1.add(jRadioButtonContaFixa);
        jRadioButtonContaFixa.setText("Conta Fixa");
        jRadioButtonContaFixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonContaFixaActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonContaHidrometro);
        jRadioButtonContaHidrometro.setText("Conta com Hidrômetro");
        jRadioButtonContaHidrometro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonContaHidrometroActionPerformed(evt);
            }
        });

        jButton1.setText("Gerar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jProgressBar1.setBackground(new java.awt.Color(0, 255, 255));
        jProgressBar1.setStringPainted(true);

        jCheckBox1.setText("Marcar todas");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton2.setText("Imprimir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Vencimento:");

        jLabel2.setText("Referência:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateReferencia, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jDateVencimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateReferencia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonContaFixa)
                            .addComponent(jRadioButtonContaHidrometro))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButtonContaFixa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jRadioButtonContaHidrometro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1)
                                    .addComponent(jCheckBox1))
                                .addGap(53, 53, 53)
                                .addComponent(jButton2))
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonContaFixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonContaFixaActionPerformed

        preencherTabela();
    }//GEN-LAST:event_jRadioButtonContaFixaActionPerformed

    private void jRadioButtonContaHidrometroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonContaHidrometroActionPerformed

        preencherTabela();
    }//GEN-LAST:event_jRadioButtonContaHidrometroActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if(jDateReferencia.getDate()==null || jDateReferencia.getDate().equals("")){
          JOptionPane.showMessageDialog(GContas.this, "Escolha a data de Refêrencia", "Atenção", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
      
       if(jTable1.getRowCount() >0){
          
        boolean existeContaSelecionada = false;
        for(int i = 0; i<jTable1.getRowCount(); i++){
            if((boolean)jTable1.getValueAt(i, 7)){                   
                existeContaSelecionada = true;
            }
        }
            
        
        //Se tiver pelo menos uma conta selecionada, o bloco if será executado.
        if(existeContaSelecionada){
         boolean consumoPreenchido = false;
         int cont = 0;
         Pattern p = Pattern.compile("\\d+");
         Matcher m = null;
         //Se as contas de hidrometro estiverem selecionadas...o bloco if irá fazer um teste para verificar se a coluna consumo está preenchida corretamente.
        if(jRadioButtonContaHidrometro.isSelected()){
            
           for(int i = 0; i<jTable1.getRowCount(); i++){
               if(jTable1.getValueAt(i, 9) != null){
                   cont++;
               m = p.matcher(String.valueOf(jTable1.getValueAt(i, 9)));
               if(m.find() && (boolean)jTable1.getValueAt(i, 7)){
                   consumoPreenchido = true;
               }
           }
           }
            
        }    
        //se a coluna de consumo estiver preenchida de acordo com a expressão regular, o bloco if será executado.    
       if(jRadioButtonContaHidrometro.isSelected()){
        if(consumoPreenchido){
        int op = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja registrar as contas desses sócios?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (op == JOptionPane.YES_OPTION) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Conta c = null;
                    jButton1.setEnabled(false);
                    jRadioButtonContaFixa.setEnabled(false);
                    jRadioButtonContaHidrometro.setEnabled(false);

                        try {

                            int cont = 0;
                            //percorre todas as linhas
                            for (int i = 0; i < model.getRowCount(); i++) {

                                if ((boolean) model.getValueAt(i, 7)) {

                                    cont++;
                                }
                            }

                            jProgressBar1.setMaximum(cont - 1);
                            //percorre todas as linhas
                            for (int i = 0; i < model.getRowCount(); i++) {
                                //se o checkbox estiver marcado executa o if.
                                if ((boolean) model.getValueAt(i, 7)) {

                                    jProgressBar1.setValue(i);

                                    //se tiver selecionado as contas fixas...
                                    if (jRadioButtonContaFixa.isSelected()) {
                                        //Cria uma conta vazia.
                                        c = new Conta();
                                        //seta a data Gerada como a data atual.
                                        c.setDataGerada(new Date());
                                        //data de vencimento da tabela
                                        c.setDataVence(SimpleDateFormat.getDateInstance().parse((String) model.getValueAt(i, 1)));
                                        //Pega do banco um endereçoPessoa de acordo com o número do endereço na tabela, que é único
                                        Enderecopessoa e = new DaoEnderecoPessoa().EnderecopessoaporNumero((String) model.getValueAt(i, 5));
                                        //seta o enderecoPessoa na conta
                                        c.setIdEnderecoPessoa(e);
                                        c.setValorTaxa(e.getIdCategoriaSocio().getTaxasId().getValor());
                                        c.setValorOutros(e.getIdCategoriaSocio().getTaxasId().getValorSocio());
                                        
                                        c.setDataReferente(jDateReferencia.getCalendar().getTime());
                                        
                                        List<Taxasconta> taxas1 = new ArrayList<>();
                                        if(taxas.containsKey(e.getNumero())){
                                       
                                            for(Taxa aux : taxas.get(e.getNumero())){
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        tx.setTaxaid(aux);
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);
                                       } 
                                       }else{
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        tx.setTaxaid(e.getIdCategoriaSocio().getTaxasId());
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);  
                                        }
                                        //lista de taxasConta na conta...
                                        //c.setTaxascontaList(taxas1);
                                        new DaoContasMensais().AdicionarConta(c);

                                    } //se tiver selecionado as contas com hidrometro
                                    else if (jRadioButtonContaHidrometro.isSelected()) {
                                        
                                         //Cria uma conta vazia.
                                        c = new Conta();
                                        //seta a data Gerada como a data atual.
                                        c.setDataGerada(new Date());
                                        //data de vencimento da tabela
                                        c.setDataVence(SimpleDateFormat.getDateInstance().parse((String) model.getValueAt(i, 1)));
                                        //Pega do banco um endereçoPessoa de acordo com o número do endereço na tabela, que é único
                                        Enderecopessoa e = new DaoEnderecoPessoa().EnderecopessoaporNumero((String) model.getValueAt(i, 5));
                                        //seta o enderecoPessoa na conta
                                        c.setIdEnderecoPessoa(e);
                                        c.setValorTaxa(e.getIdCategoriaSocio().getTaxasId().getValor());
                                        c.setValorOutros(e.getIdCategoriaSocio().getTaxasId().getValorSocio());
                                         c.setDataReferente(jDateReferencia.getCalendar().getTime());
                                       
                                        List<Taxasconta> taxas1 = new ArrayList<>();
                                        if(taxas.containsKey(e.getNumero())){
                                       for(Taxa aux : taxas.get(e.getNumero())){
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        tx.setTaxaid(aux);
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);
                                       } 
                                       }else{
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        tx.setTaxaid(e.getIdCategoriaSocio().getTaxasId());
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);  
                                        }
                                        
                                        Hidrometro h = new Hidrometro();
                                        h.setIdconta(c);
                                        h.setConsumoInicial((double)model.getValueAt(i, 9));
                                        h.setConsumoFinal((double)model.getValueAt(i, 10));
                                        //lista de taxasConta na conta...
                                       // c.setTaxascontaList(taxas1);
                                        c.setHidrometro(h);
                                        new DaoContasMensais().AdicionarConta(c); 
                                        
                                    }

                                }
                            }

                                jButton1.setEnabled(true);
                                jRadioButtonContaFixa.setEnabled(true);
                                jRadioButtonContaHidrometro.setEnabled(true);
                                JOptionPane.showMessageDialog(GContas.this, "Conta(s) Registrada(s) com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                                jProgressBar1.setValue(0);
                                jCheckBox1.setSelected(false);
                                carregarSocios();
                                preencherTabela();
                            

                        } catch (Exception e) {

                            JOptionPane.showMessageDialog(GContas.this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    
                }
            };

            t.start();
        }
        }else{
          JOptionPane.showMessageDialog(this, "Preencha corretamente o campo de consumo com dígitos","Atenção",JOptionPane.ERROR_MESSAGE);       
        }
        
        }else{
            
        int op = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja registrar as contas desses sócios?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (op == JOptionPane.YES_OPTION) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Conta c = null;
                    jButton1.setEnabled(false);
                    jRadioButtonContaFixa.setEnabled(false);
                    jRadioButtonContaHidrometro.setEnabled(false);

                        try {

                            int cont = 0;
                            //percorre todas as linhas
                            for (int i = 0; i < model.getRowCount(); i++) {

                                if ((boolean) model.getValueAt(i, 7)) {

                                    cont++;
                                }
                            }

                            jProgressBar1.setMaximum(cont - 1);
                            //percorre todas as linhas
                            for (int i = 0; i < model.getRowCount(); i++) {
                                //se o checkbox estiver marcado executa o if.
                                if ((boolean) model.getValueAt(i, 7)) {

                                    jProgressBar1.setValue(i);

                                    //se tiver selecionado as contas fixas...
                                    if (jRadioButtonContaFixa.isSelected()) {
                                        //Cria uma conta vazia.
                                        c = new Conta();
                                        //seta a data Gerada como a data atual.
                                        c.setDataGerada(new Date());
                                        //data de vencimento da tabela
                                        c.setDataVence(SimpleDateFormat.getDateInstance().parse((String) model.getValueAt(i, 1)));
                                        //Pega do banco um endereçoPessoa de acordo com o número do endereço na tabela, que é único
                                        Enderecopessoa e = new DaoEnderecoPessoa().EnderecopessoaporNumero((String) model.getValueAt(i, 5));
                                        //seta o enderecoPessoa na conta
                                        c.setIdEnderecoPessoa(e);
                                        c.setValorTaxa(e.getIdCategoriaSocio().getTaxasId().getValor());
                                        c.setValorOutros(e.getIdCategoriaSocio().getTaxasId().getValorSocio());
                                         c.setDataReferente(jDateReferencia.getCalendar().getTime());
                                        List<Taxasconta> taxas1 = new ArrayList<>();
                                        if(taxas.containsKey(e.getNumero())){
                                       for(Taxa aux : taxas.get(e.getNumero())){
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        
                                        tx.setTaxaid(aux);
                                        
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);
                                       } 
                                       }else{
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        tx.setTaxaid(e.getIdCategoriaSocio().getTaxasId());
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);  
                                        }
                                        //lista de taxasConta na conta...
                                       // c.setTaxascontaList(taxas1);
                                        new DaoContasMensais().AdicionarConta(c);

                                    } //se tiver selecionado as contas com hidrometro
                                    else if (jRadioButtonContaHidrometro.isSelected()) {
                                        
                                         //Cria uma conta vazia.
                                        c = new Conta();
                                        //seta a data Gerada como a data atual.
                                        c.setDataGerada(new Date());
                                        //data de vencimento da tabela
                                        c.setDataVence(SimpleDateFormat.getDateInstance().parse((String) model.getValueAt(i, 1)));
                                        //Pega do banco um endereçoPessoa de acordo com o número do endereço na tabela, que é único
                                        Enderecopessoa e = new DaoEnderecoPessoa().EnderecopessoaporNumero((String) model.getValueAt(i, 5));
                                        //seta o enderecoPessoa na conta
                                        c.setIdEnderecoPessoa(e);
                                        c.setDataReferente(jDateReferencia.getCalendar().getTime());
                                        
                                        List<Taxasconta> taxas1 = new ArrayList<>();
                                        if(taxas.containsKey(e.getNumero())){
                                       for(Taxa aux : taxas.get(e.getNumero())){
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        tx.setTaxaid(aux);
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);
                                       } 
                                       }else{
                                        Taxasconta tx = new Taxasconta();
                                        tx.setContaid(c);
                                        //seta a taxa em taxas Conta de acordo com a categoria socio do sócio vinculado ao endereço
                                        tx.setTaxaid(e.getIdCategoriaSocio().getTaxasId());
                                        //Aqui adiciono todas as taxas de cada endereço...
                                        taxas1.add(tx);  
                                        }
                                        
                                        Hidrometro h = new Hidrometro();
                                        h.setIdconta(c);
                                        h.setConsumoInicial((double)model.getValueAt(i, 9));
                                        h.setConsumoInicial((double)model.getValueAt(i, 10));
                                        //lista de taxasConta na conta...
                                       // c.setTaxascontaList(taxas1);
                                        c.setHidrometro(h);
                                        new DaoContasMensais().AdicionarConta(c); 
                                        
                                    }

                                }
                            }

                                jButton1.setEnabled(true);
                                jRadioButtonContaFixa.setEnabled(true);
                                jRadioButtonContaHidrometro.setEnabled(true);
                                JOptionPane.showMessageDialog(GContas.this, "Conta(s) Registrada(s) com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                                jProgressBar1.setValue(0);
                                jCheckBox1.setSelected(false);
                                carregarSocios();
                                preencherTabela();
                            
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(GContas.this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    
                }
            };

            t.start();
        } 
        }
        
        
        }else{
            
            JOptionPane.showMessageDialog(this, "Nenhum sócio foi selecionado","Atenção",JOptionPane.INFORMATION_MESSAGE);
            
        }
       }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMonthChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jMonthChooser1PropertyChange
        taxas.clear();
        jCheckBox1.setSelected(false);
        if (jRadioButtonContaFixa.isSelected()) {

            jRadioButtonContaFixaActionPerformed(null);
        } else if (jRadioButtonContaHidrometro.isSelected()) {

            jRadioButtonContaHidrometroActionPerformed(null);
        }
    }//GEN-LAST:event_jMonthChooser1PropertyChange

    private void jYearChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jYearChooser1PropertyChange
        taxas.clear();
        jCheckBox1.setSelected(false);
        if (jRadioButtonContaFixa.isSelected()) {

            jRadioButtonContaFixaActionPerformed(null);
        } else if (jRadioButtonContaHidrometro.isSelected()) {

            jRadioButtonContaHidrometroActionPerformed(null);
        }
    }//GEN-LAST:event_jYearChooser1PropertyChange

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        telaPrincipal.setEnabled(true);
        telaPrincipal.setVisible(true);
        telaPrincipal.toFront();

    }//GEN-LAST:event_formWindowClosed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
      
        
        if((jRadioButtonContaFixa.isSelected() || jRadioButtonContaHidrometro.isSelected()) && jTable1.getModel().getRowCount() >0){
        DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
            if(evt.getStateChange() == ItemEvent.SELECTED){
            
         for(int i = 0; i<model.getRowCount();i++){
             
             model.setValueAt(true, i, 7);
             
         }
            
        }else if(evt.getStateChange() == ItemEvent.DESELECTED){
            
             for(int i = 0; i<model.getRowCount();i++){
             
             model.setValueAt(false, i, 7);
             
         } 
            
        }
        
        }else{
            
            jCheckBox1.setSelected(false);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        TelaRelatoriosContas tc = new TelaRelatoriosContas();
        tc.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(GContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(GContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(GContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(GContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GContas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateReferencia;
    private com.toedter.calendar.JDateChooser jDateVencimento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButtonContaFixa;
    private javax.swing.JRadioButton jRadioButtonContaHidrometro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
