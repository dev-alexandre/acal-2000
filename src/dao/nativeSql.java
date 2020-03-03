
package dao;

public class nativeSql {
    
    private StringBuilder socioExclusivo;

    public StringBuilder getSocioExclusivo(){
   
        socioExclusivo.append(
            " select * from conta c inner join enderecopessoa ep on ep.id = c.idEnderecoPessoa inner join pessoa p on  p.id = ep.idPessoa "+
            " inner join endereco        e on  e.id = ep.idEndereco "+
            " inner join socio           s on  p.id = s.idPessoa "+
            " inner join categoriasocio cs on cs.id = ep.idCategoriaSocio "+
            " inner join taxa            t on  t.id = cs.taxasId "+
            " left  join hidrometro      h on  c.id = h.idconta "+
            " left  join taxasconta     tc on  c.id = tc.contaid "+
            " left  join taxa           t2 on  t2.id = tc.taxaid ");
        return socioExclusivo;
    }
       
}
