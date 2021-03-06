package com.millenium.credibanco.evaq.test;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import org.wso2.carbon.transport.http.netty.internal.config.Parameter;
import org.wso2.carbon.transport.http.netty.internal.config.YAMLTransportConfigurationBuilder;

import com.millenium.credibanco.evaq.engine.EvaluadorEmisorModelo;
import com.millenium.credibanco.evaq.model.ParametrosEvaluadorQ;
import com.millenium.credibanco.evaq.model.Tx;

public class PruebasFinales {

	public static void main(String[] args) throws SQLException, ParseException {
		List<Parameter> propiedadesYml = YAMLTransportConfigurationBuilder
        		.build()
        		.getListenerConfigurations()
        		.stream()
        		.reduce( (a,b)->b )
        		.get()
        		.getParameters();
        for ( Parameter p : propiedadesYml ) {
			System.setProperty( p.getName() , p.getValue() );
		}
//		DecimalFormat df = new DecimalFormat("#.#");
//		df.format(55.544545);
//        double score = ((738815.51 - 380295.77) / 1098755.09);
//        System.out.println(score);
		DbTools db= new DbTools();
		Long id=Long.valueOf(args[0]);
		boolean presente = ("SI").equals(args[1].toString())?true:false;
		boolean nacional =("SI").equals(args[2].toString())?true:false;
		Tx tx1 =db.getTx(id);
		List<Tx>listTx=db.getListTx(id);
		List<Tx> historico=db.getHistorico(id);

		List<ParametrosEvaluadorQ> parametros = db.getParametros(presente, nacional, "EMISOR");
		//System.out.println(historico.size());
		//System.out.println(tx1.getIdComercio());
		EvaluadorEmisorModelo.ejecutarEvaluador(historico, tx1, parametros,listTx);
		

	}

}
