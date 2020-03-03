package TableModel;

import entidades.Enderecopessoa;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

public class EnderecoPessoaTableModel extends AbstractTableModel{
    
    private static final int numero           = 0;
    private static final int logradouro       = 1;
    private static final int matricula        = 2;
    private static final int inativo          = 3;
    private static final int exclusivo        = 4;
    private static final int categoriaSocio   = 5;

    private  List<Enderecopessoa> linhas;
    
    public EnderecoPessoaTableModel(){
        linhas = new ArrayList<>();
    }
    
    public EnderecoPessoaTableModel(List<Enderecopessoa> listaDoCaixa) {
        linhas = new ArrayList<>(listaDoCaixa);
    }
    
    private String[] colunas = new String[] 
    {"Numero",
     "Logradouro", 
     "Matricula", 
     "Inativo",
     "Exclusivo",
     "Categoria Socio"
   
    };
   
    @Override
    public int getRowCount(){
       return linhas.size();  
    }
     
    @Override
    public int getColumnCount() {
      return colunas.length;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    };
  
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case numero:
            return String.class;
            case logradouro:
            return JComboBox.class;
            case matricula:
            return Date.class;
            case inativo:
            return Boolean.class;
            case exclusivo:
            return Boolean.class;
            case categoriaSocio:
            return JComboBox.class;
         
                        
        default:
        throw new IndexOutOfBoundsException("Index Não Encontrado");
        }
    }
     
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
   
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    
    Enderecopessoa c = linhas.get(rowIndex);
 
        switch (columnIndex) {
            case numero:
            return c.getNumero();
            case logradouro:
            return c.getIdEndereco().getTipo() +" "+c.getIdEndereco().getNome();
            case matricula:
            return c.getDatamatricula();
            case inativo:
            return c.getInativo();
            case exclusivo:
            return c.getSocioExclusivo();
            case categoriaSocio:
            return c.getIdCategoriaSocio().getNome();
        default:
        throw new IndexOutOfBoundsException("Campo Não Encontrado");
        }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    Enderecopessoa c = linhas.get(rowIndex);

     switch (columnIndex) {
            case numero:
              c.setNumero((String)aValue);
            break;
            case logradouro:
              c.getIdEndereco().setNome((String) aValue);
            break;    
            case matricula:
              c.setDatamatricula((Date)aValue);
            break;
            case inativo:
              c.setInativo((Boolean) aValue);
            break;
            case exclusivo:
              c.setSocioExclusivo((Boolean) aValue);
            break;
            case categoriaSocio:
              c.getIdCategoriaSocio().setNome((String) aValue);
            break;
      default:
      throw new IndexOutOfBoundsException("Alteração de ID Não permintida ou Index inválido");
      
    }
    fireTableCellUpdated(rowIndex, columnIndex);
     }
   
    public Enderecopessoa getLinha(int indiceLinha) {
        return linhas.get(indiceLinha);
    }
 

    public void addLinha(Enderecopessoa caixa) {
    linhas.add(caixa);

    int ultimoIndice = getRowCount() - 1;
 
    fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }
 
    public void removeLinha(int indiceLinha) {
    linhas.remove(indiceLinha);
 
    // Notifica a mudança.
    fireTableRowsDeleted(indiceLinha, indiceLinha);
    }
 
    // Adiciona uma lista de sócios no final da lista.
    public void addListaDeCaixa(List<Enderecopessoa> caixaLinha) {
        // Pega o tamanho antigo da tabela, que servirá
        // como índice para o primeiro dos novos registros
        int indice = getRowCount();
 
        // Adiciona os registros.
         linhas.addAll(caixaLinha);
 
        // Notifica a mudança.
         fireTableRowsInserted(indice, indice + caixaLinha.size());
    }
 
    // Remove todos os registros.
    public void limpar() {
        linhas.clear();
        fireTableDataChanged();
    }   


    
 
    
}
