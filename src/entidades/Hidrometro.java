package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexandre
 */
@Entity
@Table(name = "hidrometro")
@XmlRootElement

public class Hidrometro implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idhidrometro")
    private Integer idhidrometro;
   
    @Column(name = "consumo_inicial")
    private Double consumoInicial;
    
    @Column(name = "consumo_final")
    private Double consumoFinal;
    
    @JoinColumn(name = "idconta", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Conta idconta;

    public Hidrometro() {
    }

    public Hidrometro(Integer idhidrometro) {
        this.idhidrometro = idhidrometro;
    }

    public Integer getIdhidrometro() {
        return idhidrometro;
    }

    public void setIdhidrometro(Integer idhidrometro) {
        this.idhidrometro = idhidrometro;
    }

    public Double getConsumoInicial() {
        return consumoInicial;
    }

    public void setConsumoInicial(Double consumoInicial) {
        this.consumoInicial = consumoInicial;
    }

    public Double getConsumoFinal() {
        return consumoFinal;
    }

    public void setConsumoFinal(Double consumoFinal) {
        this.consumoFinal = consumoFinal;
    }


    public Conta getIdconta() {
        return idconta;
    }

    public void setIdconta(Conta idconta) {
        this.idconta = idconta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idhidrometro != null ? idhidrometro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
    
        if (!(object instanceof Hidrometro)) {
            return false;
        }
        Hidrometro other = (Hidrometro) object;
        if ((this.idhidrometro == null && other.idhidrometro != null) || (this.idhidrometro != null && !this.idhidrometro.equals(other.idhidrometro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Hidrometro[ idhidrometro=" + idhidrometro + " ]";
    }
    
}
